

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

}



