

package ci.orange.chatapi.dao.repository;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

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
import ci.orange.chatapi.dao.repository.base._ConversationRepository;

/**
 * Repository : Conversation.
 *
 * @author Geo
 */
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer>, _ConversationRepository {

    @Query("""
    SELECT c
    FROM Conversation c
    JOIN ConversationUser cu ON cu.conversation.id = c.id
    WHERE c.typeConversation.code = 'PRIVATE'
      AND c.isDeleted = false
      AND cu.isDeleted = false
      AND cu.user.id IN (:user1, :user2)
    GROUP BY c.id
    HAVING COUNT(DISTINCT cu.user.id) = 2
    """)
    Optional<Conversation> findExistingPrivateConversation(
            @Param("user1") Integer Creator,
            @Param("user2") Integer participant
    );

    /**
     * Récupère toutes les conversations actives d'un utilisateur
     * (conversations non supprimées dont l'utilisateur est membre et hasCleaned = false)
     *
     * @param userId ID de l'utilisateur
     * @return Liste des conversations actives
     */
    @Query("""
    SELECT DISTINCT c
    FROM Conversation c
    JOIN ConversationUser cu ON cu.conversation.id = c.id
    WHERE cu.user.id = :userId
      AND (c.isDeleted = false OR c.isDeleted IS NULL)
      AND (cu.hasCleaned = false OR cu.hasCleaned IS NULL)
    ORDER BY c.createdAt DESC
    """)
    List<Conversation> findActiveConversationsByUserId(@Param("userId") Integer userId);

    /**
     * Compte les conversations actives d'un utilisateur
     * (conversations non supprimées dont l'utilisateur est membre et hasCleaned = false)
     *
     * @param userId ID de l'utilisateur
     * @return Nombre de conversations actives
     */
    @Query("""
    SELECT COUNT(DISTINCT c.id)
    FROM Conversation c
    JOIN ConversationUser cu ON cu.conversation.id = c.id
    WHERE cu.user.id = :userId
      AND (c.isDeleted = false OR c.isDeleted IS NULL)
      AND (cu.hasCleaned = false OR cu.hasCleaned IS NULL)
    """)
    Long countActiveConversationsByUserId(@Param("userId") Integer userId);

}
