
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
 * Repository customize : TypeConversation.
 *
 * @author Geo
 *
 */
@Repository
public interface _TypeConversationRepository {
	    /**
     * Finds TypeConversation by using id as a search criteria.
     *
     * @param id
     * @return An Object TypeConversation whose id is equals to the given id. If
     *         no TypeConversation is found, this method returns null.
     */
    @Query("select e from TypeConversation e where e.id = :id and e.isDeleted = :isDeleted")
    TypeConversation findOne(@Param("id")Integer id, @Param("isDeleted")Boolean isDeleted);

    /**
     * Finds TypeConversation by using libelle as a search criteria.
     *
     * @param libelle
     * @return An Object TypeConversation whose libelle is equals to the given libelle. If
     *         no TypeConversation is found, this method returns null.
     */
    @Query("select e from TypeConversation e where e.libelle = :libelle and e.isDeleted = :isDeleted")
    TypeConversation findByLibelle(@Param("libelle")String libelle, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds TypeConversation by using code as a search criteria.
     *
     * @param code
     * @return An Object TypeConversation whose code is equals to the given code. If
     *         no TypeConversation is found, this method returns null.
     */
    @Query("select e from TypeConversation e where e.code = :code and e.isDeleted = :isDeleted")
    TypeConversation findByCode(@Param("code")String code, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds TypeConversation by using createdAt as a search criteria.
     *
     * @param createdAt
     * @return An Object TypeConversation whose createdAt is equals to the given createdAt. If
     *         no TypeConversation is found, this method returns null.
     */
    @Query("select e from TypeConversation e where e.createdAt = :createdAt and e.isDeleted = :isDeleted")
    List<TypeConversation> findByCreatedAt(@Param("createdAt")Date createdAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds TypeConversation by using updatedAt as a search criteria.
     *
     * @param updatedAt
     * @return An Object TypeConversation whose updatedAt is equals to the given updatedAt. If
     *         no TypeConversation is found, this method returns null.
     */
    @Query("select e from TypeConversation e where e.updatedAt = :updatedAt and e.isDeleted = :isDeleted")
    List<TypeConversation> findByUpdatedAt(@Param("updatedAt")Date updatedAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds TypeConversation by using deletedAt as a search criteria.
     *
     * @param deletedAt
     * @return An Object TypeConversation whose deletedAt is equals to the given deletedAt. If
     *         no TypeConversation is found, this method returns null.
     */
    @Query("select e from TypeConversation e where e.deletedAt = :deletedAt and e.isDeleted = :isDeleted")
    List<TypeConversation> findByDeletedAt(@Param("deletedAt")Date deletedAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds TypeConversation by using createdBy as a search criteria.
     *
     * @param createdBy
     * @return An Object TypeConversation whose createdBy is equals to the given createdBy. If
     *         no TypeConversation is found, this method returns null.
     */
    @Query("select e from TypeConversation e where e.createdBy = :createdBy and e.isDeleted = :isDeleted")
    List<TypeConversation> findByCreatedBy(@Param("createdBy")Integer createdBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds TypeConversation by using updatedBy as a search criteria.
     *
     * @param updatedBy
     * @return An Object TypeConversation whose updatedBy is equals to the given updatedBy. If
     *         no TypeConversation is found, this method returns null.
     */
    @Query("select e from TypeConversation e where e.updatedBy = :updatedBy and e.isDeleted = :isDeleted")
    List<TypeConversation> findByUpdatedBy(@Param("updatedBy")Integer updatedBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds TypeConversation by using deletedBy as a search criteria.
     *
     * @param deletedBy
     * @return An Object TypeConversation whose deletedBy is equals to the given deletedBy. If
     *         no TypeConversation is found, this method returns null.
     */
    @Query("select e from TypeConversation e where e.deletedBy = :deletedBy and e.isDeleted = :isDeleted")
    List<TypeConversation> findByDeletedBy(@Param("deletedBy")Integer deletedBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds TypeConversation by using isDeleted as a search criteria.
     *
     * @param isDeleted
     * @return An Object TypeConversation whose isDeleted is equals to the given isDeleted. If
     *         no TypeConversation is found, this method returns null.
     */
    @Query("select e from TypeConversation e where e.isDeleted = :isDeleted")
    List<TypeConversation> findByIsDeleted(@Param("isDeleted")Boolean isDeleted);



    /**
     * Finds List of TypeConversation by using typeConversationDto as a search criteria.
     *
     * @param request, em
     * @return A List of TypeConversation
     * @throws DataAccessException,ParseException
     */
    public default List<TypeConversation> getByCriteria(Request<TypeConversationDto> request, EntityManager em, Locale locale) throws DataAccessException, Exception {
        String req = "select e from TypeConversation e where e IS NOT NULL";
        HashMap<String, Object> param = new HashMap<String, Object>();
        req += getWhereExpression(request, param, locale);
                TypedQuery<TypeConversation> query = em.createQuery(req, TypeConversation.class);
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
     * Finds count of TypeConversation by using typeConversationDto as a search criteria.
     *
     * @param request, em
     * @return Number of TypeConversation
     *
     */
    public default Long count(Request<TypeConversationDto> request, EntityManager em, Locale locale) throws DataAccessException, Exception  {
        String req = "select count(e.id) from TypeConversation e where e IS NOT NULL";
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
    default String getWhereExpression(Request<TypeConversationDto> request, HashMap<String, java.lang.Object> param, Locale locale) throws Exception {
        // main query
        TypeConversationDto dto = request.getData() != null ? request.getData() : new TypeConversationDto();
        dto.setIsDeleted(false);
        String mainReq = generateCriteria(dto, param, 0, locale);
        // others query
        String othersReq = "";
        if (request.getDatas() != null && !request.getDatas().isEmpty()) {
            Integer index = 1;
            for (TypeConversationDto elt : request.getDatas()) {
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
    default String generateCriteria(TypeConversationDto dto, HashMap<String, Object> param, Integer index,  Locale locale) throws Exception{
        List<String> listOfQuery = new ArrayList<String>();
        if (dto != null) {
            if (dto.getId() != null || Utilities.searchParamIsNotEmpty(dto.getIdParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("id", dto.getId(), "e.id", "Integer", dto.getIdParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getLibelle()) || Utilities.searchParamIsNotEmpty(dto.getLibelleParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("libelle", dto.getLibelle(), "e.libelle", "String", dto.getLibelleParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getCode()) || Utilities.searchParamIsNotEmpty(dto.getCodeParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("code", dto.getCode(), "e.code", "String", dto.getCodeParam(), param, index, locale));
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

            /*List<String> listOfCustomQuery = _generateCriteria(dto, param, index, locale);
            if (Utilities.isNotEmpty(listOfCustomQuery)) {
                listOfQuery.addAll(listOfCustomQuery);
            }*/
        }
        return CriteriaUtils.getCriteriaByListOfQuery(listOfQuery);
    }
}
