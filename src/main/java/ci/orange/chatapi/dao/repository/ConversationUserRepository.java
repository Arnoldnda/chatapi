

package ci.orange.chatapi.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ci.orange.chatapi.dao.entity.*;
import ci.orange.chatapi.dao.repository.base._ConversationUserRepository;

import java.util.List;

/**
 * Repository : ConversationUser.
 *
 * @author Geo
 */
@Repository
public interface ConversationUserRepository extends JpaRepository<ConversationUser, Integer>, _ConversationUserRepository {

    @Query("SELECT cu FROM ConversationUser cu WHERE " +
            "cu.conversation.id = :conversationId AND " +
            "cu.user.id = :userId AND " +
            "(cu.isDeleted = false OR cu.isDeleted IS NULL) AND " +
            "(cu.hasLeft = false OR cu.hasLeft IS NULL) AND " +
            "(cu.hasDefinitivelyLeft = false OR cu.hasDefinitivelyLeft IS NULL)")
    ConversationUser findActiveUserInConversation(
            @Param("conversationId") int conversationId,
            @Param("userId") int userId
    );

    ConversationUser findByConversation_IdAndUser_IdAndIsDeletedFalse(Integer conversationId, Integer userId);

    @Query("SELECT COUNT(cu) FROM ConversationUser cu WHERE " +
            "cu.conversation.id = :conversationId AND " +
            "cu.role = true AND " +
            "(cu.isDeleted = false OR cu.isDeleted IS NULL) AND " +
            "(cu.hasLeft = false OR cu.hasLeft IS NULL) AND " +
            "(cu.hasDefinitivelyLeft = false OR cu.hasDefinitivelyLeft IS NULL)")
    long countActiveAdmins(@Param("conversationId") int conversationId);

    @Query("""
    SELECT cu
    FROM ConversationUser cu
    WHERE cu.conversation.id = :conversationId
      AND cu.role = false
      AND cu.user.id <> :excludeUserId
      AND (cu.isDeleted = false OR cu.isDeleted IS NULL)
      AND (cu.hasLeft = false OR cu.hasLeft IS NULL)
      AND (cu.hasDefinitivelyLeft = false OR cu.hasDefinitivelyLeft IS NULL)
    ORDER BY cu.createdAt ASC
    """)
    List<ConversationUser> findEligibleMemberForAdminTransfer(
            @Param("conversationId") int conversationId,
            @Param("excludeUserId") int excludeUserId
    );
}
