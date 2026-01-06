
package ci.orange.chatapi.dao.repository.base;

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

/**
 * Repository customize : Conversation.
 *
 * @author Geo
 *
 */
@Repository
public interface _ConversationRepository {
	    /**
     * Finds Conversation by using id as a search criteria.
     *
     * @param id
     * @return An Object Conversation whose id is equals to the given id. If
     *         no Conversation is found, this method returns null.
     */
    @Query("select e from Conversation e where e.id = :id and e.isDeleted = :isDeleted")
    Conversation findOne(@Param("id")Integer id, @Param("isDeleted")Boolean isDeleted);

    /**
     * Finds Conversation by using titre as a search criteria.
     *
     * @param titre
     * @return An Object Conversation whose titre is equals to the given titre. If
     *         no Conversation is found, this method returns null.
     */
    @Query("select e from Conversation e where e.titre = :titre and e.isDeleted = :isDeleted")
    List<Conversation> findByTitre(@Param("titre")String titre, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds Conversation by using createdAt as a search criteria.
     *
     * @param createdAt
     * @return An Object Conversation whose createdAt is equals to the given createdAt. If
     *         no Conversation is found, this method returns null.
     */
    @Query("select e from Conversation e where e.createdAt = :createdAt and e.isDeleted = :isDeleted")
    List<Conversation> findByCreatedAt(@Param("createdAt")Date createdAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds Conversation by using updatedAt as a search criteria.
     *
     * @param updatedAt
     * @return An Object Conversation whose updatedAt is equals to the given updatedAt. If
     *         no Conversation is found, this method returns null.
     */
    @Query("select e from Conversation e where e.updatedAt = :updatedAt and e.isDeleted = :isDeleted")
    List<Conversation> findByUpdatedAt(@Param("updatedAt")Date updatedAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds Conversation by using deletedAt as a search criteria.
     *
     * @param deletedAt
     * @return An Object Conversation whose deletedAt is equals to the given deletedAt. If
     *         no Conversation is found, this method returns null.
     */
    @Query("select e from Conversation e where e.deletedAt = :deletedAt and e.isDeleted = :isDeleted")
    List<Conversation> findByDeletedAt(@Param("deletedAt")Date deletedAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds Conversation by using createdBy as a search criteria.
     *
     * @param createdBy
     * @return An Object Conversation whose createdBy is equals to the given createdBy. If
     *         no Conversation is found, this method returns null.
     */
    @Query("select e from Conversation e where e.createdBy = :createdBy and e.isDeleted = :isDeleted")
    List<Conversation> findByCreatedBy(@Param("createdBy")Integer createdBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds Conversation by using updatedBy as a search criteria.
     *
     * @param updatedBy
     * @return An Object Conversation whose updatedBy is equals to the given updatedBy. If
     *         no Conversation is found, this method returns null.
     */
    @Query("select e from Conversation e where e.updatedBy = :updatedBy and e.isDeleted = :isDeleted")
    List<Conversation> findByUpdatedBy(@Param("updatedBy")Integer updatedBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds Conversation by using deletedBy as a search criteria.
     *
     * @param deletedBy
     * @return An Object Conversation whose deletedBy is equals to the given deletedBy. If
     *         no Conversation is found, this method returns null.
     */
    @Query("select e from Conversation e where e.deletedBy = :deletedBy and e.isDeleted = :isDeleted")
    List<Conversation> findByDeletedBy(@Param("deletedBy")Integer deletedBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds Conversation by using isDeleted as a search criteria.
     *
     * @param isDeleted
     * @return An Object Conversation whose isDeleted is equals to the given isDeleted. If
     *         no Conversation is found, this method returns null.
     */
    @Query("select e from Conversation e where e.isDeleted = :isDeleted")
    List<Conversation> findByIsDeleted(@Param("isDeleted")Boolean isDeleted);

    /**
     * Finds Conversation by using typeConversationId as a search criteria.
     *
     * @param typeConversationId
     * @return An Object Conversation whose typeConversationId is equals to the given typeConversationId. If
     *         no Conversation is found, this method returns null.
     */
    @Query("select e from Conversation e where e.typeConversation.id = :typeConversationId and e.isDeleted = :isDeleted")
    List<Conversation> findByTypeConversationId(@Param("typeConversationId")Integer typeConversationId, @Param("isDeleted")Boolean isDeleted);

  /**
   * Finds one Conversation by using typeConversationId as a search criteria.
   *
   * @param typeConversationId
   * @return An Object Conversation whose typeConversationId is equals to the given typeConversationId. If
   *         no Conversation is found, this method returns null.
   */
  @Query("select e from Conversation e where e.typeConversation.id = :typeConversationId and e.isDeleted = :isDeleted")
  Conversation findConversationByTypeConversationId(@Param("typeConversationId")Integer typeConversationId, @Param("isDeleted")Boolean isDeleted);




    /**
     * Finds List of Conversation by using conversationDto as a search criteria.
     *
     * @param request, em
     * @return A List of Conversation
     * @throws DataAccessException,ParseException
     */
    public default List<Conversation> getByCriteria(Request<ConversationDto> request, EntityManager em, Locale locale) throws DataAccessException, Exception {
        String req = "select e from Conversation e where e IS NOT NULL";
        HashMap<String, Object> param = new HashMap<String, Object>();
        req += getWhereExpression(request, param, locale);
                TypedQuery<Conversation> query = em.createQuery(req, Conversation.class);
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
     * Finds count of Conversation by using conversationDto as a search criteria.
     *
     * @param request, em
     * @return Number of Conversation
     *
     */
    public default Long count(Request<ConversationDto> request, EntityManager em, Locale locale) throws DataAccessException, Exception  {
        String req = "select count(e.id) from Conversation e where e IS NOT NULL";
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
    default String getWhereExpression(Request<ConversationDto> request, HashMap<String, java.lang.Object> param, Locale locale) throws Exception {
        // main query
        ConversationDto dto = request.getData() != null ? request.getData() : new ConversationDto();
        dto.setIsDeleted(false);
        String mainReq = generateCriteria(dto, param, 0, locale);
        // others query
        String othersReq = "";
        if (request.getDatas() != null && !request.getDatas().isEmpty()) {
            Integer index = 1;
            for (ConversationDto elt : request.getDatas()) {
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
    default String generateCriteria(ConversationDto dto, HashMap<String, Object> param, Integer index,  Locale locale) throws Exception{
        List<String> listOfQuery = new ArrayList<String>();
        if (dto != null) {
            if (dto.getId() != null || Utilities.searchParamIsNotEmpty(dto.getIdParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("id", dto.getId(), "e.id", "Integer", dto.getIdParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getTitre()) || Utilities.searchParamIsNotEmpty(dto.getTitreParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("titre", dto.getTitre(), "e.titre", "String", dto.getTitreParam(), param, index, locale));
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
                        if (dto.getTypeConversationId() != null || Utilities.searchParamIsNotEmpty(dto.getTypeConversationIdParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("typeConversationId", dto.getTypeConversationId(), "e.typeConversation.id", "Integer", dto.getTypeConversationIdParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getTypeConversationLibelle()) || Utilities.searchParamIsNotEmpty(dto.getTypeConversationLibelleParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("typeConversationLibelle", dto.getTypeConversationLibelle(), "e.typeConversation.libelle", "String", dto.getTypeConversationLibelleParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getTypeConversationCode()) || Utilities.searchParamIsNotEmpty(dto.getTypeConversationCodeParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("typeConversationCode", dto.getTypeConversationCode(), "e.typeConversation.code", "String", dto.getTypeConversationCodeParam(), param, index, locale));
            }

            /*List<String> listOfCustomQuery = _generateCriteria(dto, param, index, locale);
            if (Utilities.isNotEmpty(listOfCustomQuery)) {
                listOfQuery.addAll(listOfCustomQuery);
            }*/
        }
        return CriteriaUtils.getCriteriaByListOfQuery(listOfQuery);
    }
}
