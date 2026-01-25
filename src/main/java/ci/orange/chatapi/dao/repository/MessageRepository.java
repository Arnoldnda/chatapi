

package ci.orange.chatapi.dao.repository;

import java.util.List;
import java.util.Optional;

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

    /**
     * Récupère les messages visibles pour un utilisateur en combinant :
     * 1. les contraintes visibilité (absences/retours)
     * 2. Les suppressions locales (historique_suppression_message)
     *
     * @param conversationId ID de la conversation
     * @param userId ID de l'utilisateur
     * @return Liste des messages visibles pour l'utilisateur, triés par date décroissante
     */
    @Query("""
SELECT m
FROM Message m
JOIN ConversationUser cu ON cu.conversation.id = m.conversation.id
LEFT JOIN HistoriqueSuppressionMessage hsm 
  ON hsm.message.id = m.id 
  AND hsm.user.id = :userId 
  AND (hsm.isDeleted = false OR hsm.isDeleted IS NULL)
WHERE m.conversation.id = :conversationId
  AND cu.user.id = :userId
  AND (m.isDeleted = false OR m.isDeleted IS NULL)
  AND (cu.isDeleted = false OR cu.isDeleted IS NULL)
  AND hsm.id IS NULL
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
    List<Message> findMessagesForUserComplete(
            @Param("conversationId") Integer conversationId,
            @Param("userId") Integer userId
    );


    /**
     * Récupère LE dernier message visible pour un utilisateur
     * toujours en tenant compte des critères
     *
     * @param conversationId ID de la conversation
     * @param userId ID de l'utilisateur
     * @return Le dernier message visible (ou empty si aucun)
     */
    @Query("""
SELECT m
FROM Message m
JOIN ConversationUser cu ON cu.conversation.id = m.conversation.id
LEFT JOIN HistoriqueSuppressionMessage hsm
  ON hsm.message.id = m.id
  AND hsm.user.id = :userId
  AND (hsm.isDeleted = false OR hsm.isDeleted IS NULL)
WHERE m.conversation.id = :conversationId
  AND cu.user.id = :userId
  AND (m.isDeleted = false OR m.isDeleted IS NULL)
  AND (cu.isDeleted = false OR cu.isDeleted IS NULL)
  AND hsm.id IS NULL
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
LIMIT 1
""")
    Optional<Message> findLastMessageForUser(
            @Param("conversationId") Integer conversationId,
            @Param("userId") Integer userId
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
    List<Message> findMessagesForUserWithFilter(
            @Param("conversationId") Integer conversationId,
            @Param("userId") Integer userId
    );



}



