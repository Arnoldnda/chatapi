

package ci.orange.chatapi.dao.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ci.orange.chatapi.dao.entity.*;
import ci.orange.chatapi.dao.repository.base._HistoriqueSuppressionMessageRepository;

/**
 * Repository : HistoriqueSuppressionMessage.
 *
 * @author Geo
 */
@Repository
public interface HistoriqueSuppressionMessageRepository extends JpaRepository<HistoriqueSuppressionMessage, Integer>, _HistoriqueSuppressionMessageRepository {
    Optional<HistoriqueSuppressionMessage> findByMessage_IdAndUser_IdAndIsDeletedFalse(
            @Param("messageId") Integer messageId,
            @Param("userId") Integer userId
    );

}
