

package ci.orange.chatapi.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ci.orange.chatapi.dao.entity.*;
import ci.orange.chatapi.dao.repository.base._MessageRepository;

/**
 * Repository : Message.
 *
 * @author Geo
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>, _MessageRepository {

    @Query("""
SELECT m 
FROM Message m 
LEFT JOIN HistoriqueSuppressionMessage hsm 
  ON hsm.message.id = m.id 
  AND hsm.user.id = :userId 
  AND (hsm.isDeleted = false OR hsm.isDeleted IS NULL)
WHERE m.conversation.id = :conversationId 
  AND (m.isDeleted = false OR m.isDeleted IS NULL)
  AND hsm.id IS NULL
ORDER BY m.createdAt ASC
""")
    List<Message> findMessagesForUser(
            @Param("conversationId") Integer conversationId,
            @Param("userId") Integer userId
    );

    @Query("""
    SELECT m
    FROM Message m
    WHERE m.conversation.id = :conversationId
      AND (m.isDeleted = false OR m.isDeleted IS NULL)
      AND m.id NOT IN (
          SELECT h.message.id
          FROM HistoriqueSuppressionMessage h
      )
    ORDER BY m.createdAt DESC
""")
    List<Message> findLastVisibleMessageByConversation(
            @Param("conversationId") Integer conversationId
    );

    /**
     * Récupère les messages d'une conversation pour un utilisateur en tenant compte :
     * - Des messages après la création de l'utilisateur dans le groupe (ConversationUser.createdAt)
     * - Des périodes d'absence (leftAt -> recreatedAt)
     * - Du cas où l'utilisateur a quitté définitivement (definitivelyLeftAt)
     * 
     * @param conversationId ID de la conversation
     * @param userId ID de l'utilisateur
     * @return Liste des messages visibles pour l'utilisateur
     */
    @Query("""
SELECT m
FROM Message m
JOIN ConversationUser cu ON cu.conversation.id = m.conversation.id
WHERE m.conversation.id = :conversationId
  AND cu.user.id = :userId
  AND (m.isDeleted = false OR m.isDeleted IS NULL)
  AND (cu.isDeleted = false OR cu.isDeleted IS NULL)
  AND m.createdAt >= cu.createdAt
  AND (
      (cu.leftAt IS NULL)
    
      OR (cu.leftAt IS NOT NULL
          AND cu.recreatedAt IS NULL
          AND m.createdAt < cu.leftAt)
      
      OR (cu.leftAt IS NOT NULL
          AND cu.recreatedAt IS NOT NULL
          AND (cu.hasDefinitivelyLeft = false OR cu.hasDefinitivelyLeft IS NULL)
          AND (m.createdAt < cu.leftAt OR m.createdAt >= cu.recreatedAt))
      
      OR (cu.leftAt IS NOT NULL
          AND cu.recreatedAt IS NOT NULL
          AND cu.hasDefinitivelyLeft = true
          AND cu.definitivelyLeftAt IS NOT NULL
          AND (m.createdAt < cu.leftAt OR
               (m.createdAt >= cu.recreatedAt AND m.createdAt < cu.definitivelyLeftAt)))
  )
ORDER BY m.createdAt DESC
""")
    List<Message> findMessagesForUserWithAbsenceFilter(
            @Param("conversationId") Integer conversationId,
            @Param("userId") Integer userId
    );



}



