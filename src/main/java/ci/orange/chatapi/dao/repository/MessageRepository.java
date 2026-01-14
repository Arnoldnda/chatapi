

package ci.orange.chatapi.dao.repository;

import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Locale;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ci.orange.chatapi.utils.*;
import ci.orange.chatapi.utils.dto.*;
import ci.orange.chatapi.utils.contract.*;
import ci.orange.chatapi.utils.contract.Request;
import ci.orange.chatapi.utils.contract.Response;
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

}
