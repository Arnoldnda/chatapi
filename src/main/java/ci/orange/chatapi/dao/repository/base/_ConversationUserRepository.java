
package ci.orange.chatapi.dao.repository.base;

import java.util.Date;
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

/**
 * Repository customize : ConversationUser.
 *
 * @author Geo
 *
 */
@Repository
public interface _ConversationUserRepository {
	    /**
     * Finds ConversationUser by using id as a search criteria.
     *
     * @param id
     * @return An Object ConversationUser whose id is equals to the given id. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.id = :id and e.isDeleted = :isDeleted")
    ConversationUser findOne(@Param("id")Integer id, @Param("isDeleted")Boolean isDeleted);

    /**
     * Finds ConversationUser by using hasLeft as a search criteria.
     *
     * @param hasLeft
     * @return An Object ConversationUser whose hasLeft is equals to the given hasLeft. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.hasLeft = :hasLeft and e.isDeleted = :isDeleted")
    List<ConversationUser> findByHasLeft(@Param("hasLeft")Boolean hasLeft, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds ConversationUser by using leftAt as a search criteria.
     *
     * @param leftAt
     * @return An Object ConversationUser whose leftAt is equals to the given leftAt. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.leftAt = :leftAt and e.isDeleted = :isDeleted")
    List<ConversationUser> findByLeftAt(@Param("leftAt")Date leftAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds ConversationUser by using leftBy as a search criteria.
     *
     * @param leftBy
     * @return An Object ConversationUser whose leftBy is equals to the given leftBy. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.leftBy = :leftBy and e.isDeleted = :isDeleted")
    List<ConversationUser> findByLeftBy(@Param("leftBy")Integer leftBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds ConversationUser by using hasDefinitivelyLeft as a search criteria.
     *
     * @param hasDefinitivelyLeft
     * @return An Object ConversationUser whose hasDefinitivelyLeft is equals to the given hasDefinitivelyLeft. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.hasDefinitivelyLeft = :hasDefinitivelyLeft and e.isDeleted = :isDeleted")
    List<ConversationUser> findByHasDefinitivelyLeft(@Param("hasDefinitivelyLeft")Boolean hasDefinitivelyLeft, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds ConversationUser by using definitivelyLeftAt as a search criteria.
     *
     * @param definitivelyLeftAt
     * @return An Object ConversationUser whose definitivelyLeftAt is equals to the given definitivelyLeftAt. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.definitivelyLeftAt = :definitivelyLeftAt and e.isDeleted = :isDeleted")
    List<ConversationUser> findByDefinitivelyLeftAt(@Param("definitivelyLeftAt")Date definitivelyLeftAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds ConversationUser by using definitivelyLeftBy as a search criteria.
     *
     * @param definitivelyLeftBy
     * @return An Object ConversationUser whose definitivelyLeftBy is equals to the given definitivelyLeftBy. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.definitivelyLeftBy = :definitivelyLeftBy and e.isDeleted = :isDeleted")
    List<ConversationUser> findByDefinitivelyLeftBy(@Param("definitivelyLeftBy")Integer definitivelyLeftBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds ConversationUser by using recreatedAt as a search criteria.
     *
     * @param recreatedAt
     * @return An Object ConversationUser whose recreatedAt is equals to the given recreatedAt. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.recreatedAt = :recreatedAt and e.isDeleted = :isDeleted")
    List<ConversationUser> findByRecreatedAt(@Param("recreatedAt")Date recreatedAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds ConversationUser by using recreatedBy as a search criteria.
     *
     * @param recreatedBy
     * @return An Object ConversationUser whose recreatedBy is equals to the given recreatedBy. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.recreatedBy = :recreatedBy and e.isDeleted = :isDeleted")
    List<ConversationUser> findByRecreatedBy(@Param("recreatedBy")Integer recreatedBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds ConversationUser by using hasCleaned as a search criteria.
     *
     * @param hasCleaned
     * @return An Object ConversationUser whose hasCleaned is equals to the given hasCleaned. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.hasCleaned = :hasCleaned and e.isDeleted = :isDeleted")
    List<ConversationUser> findByHasCleaned(@Param("hasCleaned")Boolean hasCleaned, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds ConversationUser by using createdAt as a search criteria.
     *
     * @param createdAt
     * @return An Object ConversationUser whose createdAt is equals to the given createdAt. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.createdAt = :createdAt and e.isDeleted = :isDeleted")
    List<ConversationUser> findByCreatedAt(@Param("createdAt")Date createdAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds ConversationUser by using updatedAt as a search criteria.
     *
     * @param updatedAt
     * @return An Object ConversationUser whose updatedAt is equals to the given updatedAt. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.updatedAt = :updatedAt and e.isDeleted = :isDeleted")
    List<ConversationUser> findByUpdatedAt(@Param("updatedAt")Date updatedAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds ConversationUser by using deletedAt as a search criteria.
     *
     * @param deletedAt
     * @return An Object ConversationUser whose deletedAt is equals to the given deletedAt. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.deletedAt = :deletedAt and e.isDeleted = :isDeleted")
    List<ConversationUser> findByDeletedAt(@Param("deletedAt")Date deletedAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds ConversationUser by using createdBy as a search criteria.
     *
     * @param createdBy
     * @return An Object ConversationUser whose createdBy is equals to the given createdBy. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.createdBy = :createdBy and e.isDeleted = :isDeleted")
    List<ConversationUser> findByCreatedBy(@Param("createdBy")Integer createdBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds ConversationUser by using updatedBy as a search criteria.
     *
     * @param updatedBy
     * @return An Object ConversationUser whose updatedBy is equals to the given updatedBy. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.updatedBy = :updatedBy and e.isDeleted = :isDeleted")
    List<ConversationUser> findByUpdatedBy(@Param("updatedBy")Integer updatedBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds ConversationUser by using deletedBy as a search criteria.
     *
     * @param deletedBy
     * @return An Object ConversationUser whose deletedBy is equals to the given deletedBy. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.deletedBy = :deletedBy and e.isDeleted = :isDeleted")
    List<ConversationUser> findByDeletedBy(@Param("deletedBy")Integer deletedBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds ConversationUser by using isDeleted as a search criteria.
     *
     * @param isDeleted
     * @return An Object ConversationUser whose isDeleted is equals to the given isDeleted. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.isDeleted = :isDeleted")
    List<ConversationUser> findByIsDeleted(@Param("isDeleted")Boolean isDeleted);

    /**
     * Finds ConversationUser by using userId as a search criteria.
     *
     * @param userId
     * @return An Object ConversationUser whose userId is equals to the given userId. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.user.id = :userId and e.isDeleted = :isDeleted")
    List<ConversationUser> findByUserId(@Param("userId")Integer userId, @Param("isDeleted")Boolean isDeleted);

  /**
   * Finds one ConversationUser by using userId as a search criteria.
   *
   * @param userId
   * @return An Object ConversationUser whose userId is equals to the given userId. If
   *         no ConversationUser is found, this method returns null.
   */
  @Query("select e from ConversationUser e where e.user.id = :userId and e.isDeleted = :isDeleted")
  ConversationUser findConversationUserByUserId(@Param("userId")Integer userId, @Param("isDeleted")Boolean isDeleted);


    /**
     * Finds ConversationUser by using conversationId as a search criteria.
     *
     * @param conversationId
     * @return An Object ConversationUser whose conversationId is equals to the given conversationId. If
     *         no ConversationUser is found, this method returns null.
     */
    @Query("select e from ConversationUser e where e.conversation.id = :conversationId and e.isDeleted = :isDeleted")
    List<ConversationUser> findByConversationId(@Param("conversationId")Integer conversationId, @Param("isDeleted")Boolean isDeleted);

  /**
   * Finds one ConversationUser by using conversationId as a search criteria.
   *
   * @param conversationId
   * @return An Object ConversationUser whose conversationId is equals to the given conversationId. If
   *         no ConversationUser is found, this method returns null.
   */
  @Query("select e from ConversationUser e where e.conversation.id = :conversationId and e.isDeleted = :isDeleted")
  ConversationUser findConversationUserByConversationId(@Param("conversationId")Integer conversationId, @Param("isDeleted")Boolean isDeleted);




    /**
     * Finds List of ConversationUser by using conversationUserDto as a search criteria.
     *
     * @param request, em
     * @return A List of ConversationUser
     * @throws DataAccessException,ParseException
     */
    public default List<ConversationUser> getByCriteria(Request<ConversationUserDto> request, EntityManager em, Locale locale) throws DataAccessException, Exception {
        String req = "select e from ConversationUser e where e IS NOT NULL";
        HashMap<String, Object> param = new HashMap<String, Object>();
        req += getWhereExpression(request, param, locale);
                TypedQuery<ConversationUser> query = em.createQuery(req, ConversationUser.class);
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        if (request.getIndex() != null && request.getSize() != null) {
            query.setFirstResult(request.getIndex() * request.getSize());
            query.setMaxResults(request.getSize());
        }
        return query.getResultList();
    }

    /**
     * Finds count of ConversationUser by using conversationUserDto as a search criteria.
     *
     * @param request, em
     * @return Number of ConversationUser
     *
     */
    public default Long count(Request<ConversationUserDto> request, EntityManager em, Locale locale) throws DataAccessException, Exception  {
        String req = "select count(e.id) from ConversationUser e where e IS NOT NULL";
        HashMap<String, Object> param = new HashMap<String, Object>();
        req += getWhereExpression(request, param, locale);
                jakarta.persistence.Query query = em.createQuery(req);
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        Long count = (Long) query.getResultList().get(0);
        return count;
    }

    /**
     * get where expression
     * @param request
     * @param param
     * @param locale
     * @return
     * @throws Exception
     */
    default String getWhereExpression(Request<ConversationUserDto> request, HashMap<String, java.lang.Object> param, Locale locale) throws Exception {
        // main query
        ConversationUserDto dto = request.getData() != null ? request.getData() : new ConversationUserDto();
        dto.setIsDeleted(false);
        String mainReq = generateCriteria(dto, param, 0, locale);
        // others query
        String othersReq = "";
        if (request.getDatas() != null && !request.getDatas().isEmpty()) {
            Integer index = 1;
            for (ConversationUserDto elt : request.getDatas()) {
                elt.setIsDeleted(false);
                String eltReq = generateCriteria(elt, param, index, locale);
                if (request.getIsAnd() != null && request.getIsAnd()) {
                    othersReq += "and (" + eltReq + ") ";
                } else {
                    othersReq += "or (" + eltReq + ") ";
                }
                index++;
            }
        }
        String req = "";
        if (!mainReq.isEmpty()) {
            req += " and (" + mainReq + ") ";
        }
        req += othersReq;

        //order
        if(Direction.fromOptionalString(dto.getOrderDirection()).orElse(null) != null && Utilities.notBlank(dto.getOrderField())) {
            req += " order by e."+dto.getOrderField()+" "+dto.getOrderDirection();
        }
        else {
            req += " order by  e.id desc";
        }
        return req;
    }

    /**
     * generate sql query for dto
     * @param dto
     * @param param
     * @param index
     * @param locale
     * @return
     * @throws Exception
     */
    default String generateCriteria(ConversationUserDto dto, HashMap<String, Object> param, Integer index,  Locale locale) throws Exception{
        List<String> listOfQuery = new ArrayList<String>();
        if (dto != null) {
            if (dto.getId() != null || Utilities.searchParamIsNotEmpty(dto.getIdParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("id", dto.getId(), "e.id", "Integer", dto.getIdParam(), param, index, locale));
            }
            if (dto.getHasLeft() != null || Utilities.searchParamIsNotEmpty(dto.getHasLeftParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("hasLeft", dto.getHasLeft(), "e.hasLeft", "Boolean", dto.getHasLeftParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getLeftAt()) || Utilities.searchParamIsNotEmpty(dto.getLeftAtParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("leftAt", dto.getLeftAt(), "e.leftAt", "Date", dto.getLeftAtParam(), param, index, locale));
            }
            if (dto.getLeftBy() != null || Utilities.searchParamIsNotEmpty(dto.getLeftByParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("leftBy", dto.getLeftBy(), "e.leftBy", "Integer", dto.getLeftByParam(), param, index, locale));
            }
            if (dto.getHasDefinitivelyLeft() != null || Utilities.searchParamIsNotEmpty(dto.getHasDefinitivelyLeftParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("hasDefinitivelyLeft", dto.getHasDefinitivelyLeft(), "e.hasDefinitivelyLeft", "Boolean", dto.getHasDefinitivelyLeftParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getDefinitivelyLeftAt()) || Utilities.searchParamIsNotEmpty(dto.getDefinitivelyLeftAtParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("definitivelyLeftAt", dto.getDefinitivelyLeftAt(), "e.definitivelyLeftAt", "Date", dto.getDefinitivelyLeftAtParam(), param, index, locale));
            }
            if (dto.getDefinitivelyLeftBy() != null || Utilities.searchParamIsNotEmpty(dto.getDefinitivelyLeftByParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("definitivelyLeftBy", dto.getDefinitivelyLeftBy(), "e.definitivelyLeftBy", "Integer", dto.getDefinitivelyLeftByParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getRecreatedAt()) || Utilities.searchParamIsNotEmpty(dto.getRecreatedAtParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("recreatedAt", dto.getRecreatedAt(), "e.recreatedAt", "Date", dto.getRecreatedAtParam(), param, index, locale));
            }
            if (dto.getRecreatedBy() != null || Utilities.searchParamIsNotEmpty(dto.getRecreatedByParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("recreatedBy", dto.getRecreatedBy(), "e.recreatedBy", "Integer", dto.getRecreatedByParam(), param, index, locale));
            }
            if (dto.getHasCleaned() != null || Utilities.searchParamIsNotEmpty(dto.getHasCleanedParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("hasCleaned", dto.getHasCleaned(), "e.hasCleaned", "Boolean", dto.getHasCleanedParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getCreatedAt()) || Utilities.searchParamIsNotEmpty(dto.getCreatedAtParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("createdAt", dto.getCreatedAt(), "e.createdAt", "Date", dto.getCreatedAtParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getUpdatedAt()) || Utilities.searchParamIsNotEmpty(dto.getUpdatedAtParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("updatedAt", dto.getUpdatedAt(), "e.updatedAt", "Date", dto.getUpdatedAtParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getDeletedAt()) || Utilities.searchParamIsNotEmpty(dto.getDeletedAtParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("deletedAt", dto.getDeletedAt(), "e.deletedAt", "Date", dto.getDeletedAtParam(), param, index, locale));
            }
            if (dto.getCreatedBy() != null || Utilities.searchParamIsNotEmpty(dto.getCreatedByParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("createdBy", dto.getCreatedBy(), "e.createdBy", "Integer", dto.getCreatedByParam(), param, index, locale));
            }
            if (dto.getUpdatedBy() != null || Utilities.searchParamIsNotEmpty(dto.getUpdatedByParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("updatedBy", dto.getUpdatedBy(), "e.updatedBy", "Integer", dto.getUpdatedByParam(), param, index, locale));
            }
            if (dto.getDeletedBy() != null || Utilities.searchParamIsNotEmpty(dto.getDeletedByParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("deletedBy", dto.getDeletedBy(), "e.deletedBy", "Integer", dto.getDeletedByParam(), param, index, locale));
            }
            if (dto.getIsDeleted() != null || Utilities.searchParamIsNotEmpty(dto.getIsDeletedParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("isDeleted", dto.getIsDeleted(), "e.isDeleted", "Boolean", dto.getIsDeletedParam(), param, index, locale));
            }
                        if (dto.getUserId() != null || Utilities.searchParamIsNotEmpty(dto.getUserIdParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("userId", dto.getUserId(), "e.user.id", "Integer", dto.getUserIdParam(), param, index, locale));
            }
                        if (dto.getConversationId() != null || Utilities.searchParamIsNotEmpty(dto.getConversationIdParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("conversationId", dto.getConversationId(), "e.conversation.id", "Integer", dto.getConversationIdParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getUserNom()) || Utilities.searchParamIsNotEmpty(dto.getUserNomParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("userNom", dto.getUserNom(), "e.user.nom", "String", dto.getUserNomParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getUserPrenoms()) || Utilities.searchParamIsNotEmpty(dto.getUserPrenomsParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("userPrenoms", dto.getUserPrenoms(), "e.user.prenoms", "String", dto.getUserPrenomsParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getUserLogin()) || Utilities.searchParamIsNotEmpty(dto.getUserLoginParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("userLogin", dto.getUserLogin(), "e.user.login", "String", dto.getUserLoginParam(), param, index, locale));
            }

            /*List<String> listOfCustomQuery = _generateCriteria(dto, param, index, locale);
            if (Utilities.isNotEmpty(listOfCustomQuery)) {
                listOfQuery.addAll(listOfCustomQuery);
            }*/
        }
        return CriteriaUtils.getCriteriaByListOfQuery(listOfQuery);
    }
}
