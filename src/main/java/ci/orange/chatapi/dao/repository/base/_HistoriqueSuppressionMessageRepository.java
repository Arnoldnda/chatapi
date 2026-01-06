
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
 * Repository customize : HistoriqueSuppressionMessage.
 *
 * @author Geo
 *
 */
@Repository
public interface _HistoriqueSuppressionMessageRepository {
	    /**
     * Finds HistoriqueSuppressionMessage by using id as a search criteria.
     *
     * @param id
     * @return An Object HistoriqueSuppressionMessage whose id is equals to the given id. If
     *         no HistoriqueSuppressionMessage is found, this method returns null.
     */
    @Query("select e from HistoriqueSuppressionMessage e where e.id = :id and e.isDeleted = :isDeleted")
    HistoriqueSuppressionMessage findOne(@Param("id")Integer id, @Param("isDeleted")Boolean isDeleted);

    /**
     * Finds HistoriqueSuppressionMessage by using isHidden as a search criteria.
     *
     * @param isHidden
     * @return An Object HistoriqueSuppressionMessage whose isHidden is equals to the given isHidden. If
     *         no HistoriqueSuppressionMessage is found, this method returns null.
     */
    @Query("select e from HistoriqueSuppressionMessage e where e.isHidden = :isHidden and e.isDeleted = :isDeleted")
    List<HistoriqueSuppressionMessage> findByIsHidden(@Param("isHidden")Boolean isHidden, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds HistoriqueSuppressionMessage by using createdAt as a search criteria.
     *
     * @param createdAt
     * @return An Object HistoriqueSuppressionMessage whose createdAt is equals to the given createdAt. If
     *         no HistoriqueSuppressionMessage is found, this method returns null.
     */
    @Query("select e from HistoriqueSuppressionMessage e where e.createdAt = :createdAt and e.isDeleted = :isDeleted")
    List<HistoriqueSuppressionMessage> findByCreatedAt(@Param("createdAt")Date createdAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds HistoriqueSuppressionMessage by using updatedAt as a search criteria.
     *
     * @param updatedAt
     * @return An Object HistoriqueSuppressionMessage whose updatedAt is equals to the given updatedAt. If
     *         no HistoriqueSuppressionMessage is found, this method returns null.
     */
    @Query("select e from HistoriqueSuppressionMessage e where e.updatedAt = :updatedAt and e.isDeleted = :isDeleted")
    List<HistoriqueSuppressionMessage> findByUpdatedAt(@Param("updatedAt")Date updatedAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds HistoriqueSuppressionMessage by using deletedAt as a search criteria.
     *
     * @param deletedAt
     * @return An Object HistoriqueSuppressionMessage whose deletedAt is equals to the given deletedAt. If
     *         no HistoriqueSuppressionMessage is found, this method returns null.
     */
    @Query("select e from HistoriqueSuppressionMessage e where e.deletedAt = :deletedAt and e.isDeleted = :isDeleted")
    List<HistoriqueSuppressionMessage> findByDeletedAt(@Param("deletedAt")Date deletedAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds HistoriqueSuppressionMessage by using createdBy as a search criteria.
     *
     * @param createdBy
     * @return An Object HistoriqueSuppressionMessage whose createdBy is equals to the given createdBy. If
     *         no HistoriqueSuppressionMessage is found, this method returns null.
     */
    @Query("select e from HistoriqueSuppressionMessage e where e.createdBy = :createdBy and e.isDeleted = :isDeleted")
    List<HistoriqueSuppressionMessage> findByCreatedBy(@Param("createdBy")Integer createdBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds HistoriqueSuppressionMessage by using updatedBy as a search criteria.
     *
     * @param updatedBy
     * @return An Object HistoriqueSuppressionMessage whose updatedBy is equals to the given updatedBy. If
     *         no HistoriqueSuppressionMessage is found, this method returns null.
     */
    @Query("select e from HistoriqueSuppressionMessage e where e.updatedBy = :updatedBy and e.isDeleted = :isDeleted")
    List<HistoriqueSuppressionMessage> findByUpdatedBy(@Param("updatedBy")Integer updatedBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds HistoriqueSuppressionMessage by using deletedBy as a search criteria.
     *
     * @param deletedBy
     * @return An Object HistoriqueSuppressionMessage whose deletedBy is equals to the given deletedBy. If
     *         no HistoriqueSuppressionMessage is found, this method returns null.
     */
    @Query("select e from HistoriqueSuppressionMessage e where e.deletedBy = :deletedBy and e.isDeleted = :isDeleted")
    List<HistoriqueSuppressionMessage> findByDeletedBy(@Param("deletedBy")Integer deletedBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds HistoriqueSuppressionMessage by using isDeleted as a search criteria.
     *
     * @param isDeleted
     * @return An Object HistoriqueSuppressionMessage whose isDeleted is equals to the given isDeleted. If
     *         no HistoriqueSuppressionMessage is found, this method returns null.
     */
    @Query("select e from HistoriqueSuppressionMessage e where e.isDeleted = :isDeleted")
    List<HistoriqueSuppressionMessage> findByIsDeleted(@Param("isDeleted")Boolean isDeleted);

    /**
     * Finds HistoriqueSuppressionMessage by using userId as a search criteria.
     *
     * @param userId
     * @return An Object HistoriqueSuppressionMessage whose userId is equals to the given userId. If
     *         no HistoriqueSuppressionMessage is found, this method returns null.
     */
    @Query("select e from HistoriqueSuppressionMessage e where e.user.id = :userId and e.isDeleted = :isDeleted")
    List<HistoriqueSuppressionMessage> findByUserId(@Param("userId")Integer userId, @Param("isDeleted")Boolean isDeleted);

  /**
   * Finds one HistoriqueSuppressionMessage by using userId as a search criteria.
   *
   * @param userId
   * @return An Object HistoriqueSuppressionMessage whose userId is equals to the given userId. If
   *         no HistoriqueSuppressionMessage is found, this method returns null.
   */
  @Query("select e from HistoriqueSuppressionMessage e where e.user.id = :userId and e.isDeleted = :isDeleted")
  HistoriqueSuppressionMessage findHistoriqueSuppressionMessageByUserId(@Param("userId")Integer userId, @Param("isDeleted")Boolean isDeleted);


    /**
     * Finds HistoriqueSuppressionMessage by using messageId as a search criteria.
     *
     * @param messageId
     * @return An Object HistoriqueSuppressionMessage whose messageId is equals to the given messageId. If
     *         no HistoriqueSuppressionMessage is found, this method returns null.
     */
    @Query("select e from HistoriqueSuppressionMessage e where e.message.id = :messageId and e.isDeleted = :isDeleted")
    List<HistoriqueSuppressionMessage> findByMessageId(@Param("messageId")Integer messageId, @Param("isDeleted")Boolean isDeleted);

  /**
   * Finds one HistoriqueSuppressionMessage by using messageId as a search criteria.
   *
   * @param messageId
   * @return An Object HistoriqueSuppressionMessage whose messageId is equals to the given messageId. If
   *         no HistoriqueSuppressionMessage is found, this method returns null.
   */
  @Query("select e from HistoriqueSuppressionMessage e where e.message.id = :messageId and e.isDeleted = :isDeleted")
  HistoriqueSuppressionMessage findHistoriqueSuppressionMessageByMessageId(@Param("messageId")Integer messageId, @Param("isDeleted")Boolean isDeleted);




    /**
     * Finds List of HistoriqueSuppressionMessage by using historiqueSuppressionMessageDto as a search criteria.
     *
     * @param request, em
     * @return A List of HistoriqueSuppressionMessage
     * @throws DataAccessException,ParseException
     */
    public default List<HistoriqueSuppressionMessage> getByCriteria(Request<HistoriqueSuppressionMessageDto> request, EntityManager em, Locale locale) throws DataAccessException, Exception {
        String req = "select e from HistoriqueSuppressionMessage e where e IS NOT NULL";
        HashMap<String, Object> param = new HashMap<String, Object>();
        req += getWhereExpression(request, param, locale);
                TypedQuery<HistoriqueSuppressionMessage> query = em.createQuery(req, HistoriqueSuppressionMessage.class);
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
     * Finds count of HistoriqueSuppressionMessage by using historiqueSuppressionMessageDto as a search criteria.
     *
     * @param request, em
     * @return Number of HistoriqueSuppressionMessage
     *
     */
    public default Long count(Request<HistoriqueSuppressionMessageDto> request, EntityManager em, Locale locale) throws DataAccessException, Exception  {
        String req = "select count(e.id) from HistoriqueSuppressionMessage e where e IS NOT NULL";
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
    default String getWhereExpression(Request<HistoriqueSuppressionMessageDto> request, HashMap<String, java.lang.Object> param, Locale locale) throws Exception {
        // main query
        HistoriqueSuppressionMessageDto dto = request.getData() != null ? request.getData() : new HistoriqueSuppressionMessageDto();
        dto.setIsDeleted(false);
        String mainReq = generateCriteria(dto, param, 0, locale);
        // others query
        String othersReq = "";
        if (request.getDatas() != null && !request.getDatas().isEmpty()) {
            Integer index = 1;
            for (HistoriqueSuppressionMessageDto elt : request.getDatas()) {
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
    default String generateCriteria(HistoriqueSuppressionMessageDto dto, HashMap<String, Object> param, Integer index,  Locale locale) throws Exception{
        List<String> listOfQuery = new ArrayList<String>();
        if (dto != null) {
            if (dto.getId() != null || Utilities.searchParamIsNotEmpty(dto.getIdParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("id", dto.getId(), "e.id", "Integer", dto.getIdParam(), param, index, locale));
            }
            if (dto.getIsHidden() != null || Utilities.searchParamIsNotEmpty(dto.getIsHiddenParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("isHidden", dto.getIsHidden(), "e.isHidden", "Boolean", dto.getIsHiddenParam(), param, index, locale));
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
                        if (dto.getMessageId() != null || Utilities.searchParamIsNotEmpty(dto.getMessageIdParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("messageId", dto.getMessageId(), "e.message.id", "Integer", dto.getMessageIdParam(), param, index, locale));
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
