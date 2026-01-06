
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
 * Repository customize : TypeMessage.
 *
 * @author Geo
 *
 */
@Repository
public interface _TypeMessageRepository {
	    /**
     * Finds TypeMessage by using id as a search criteria.
     *
     * @param id
     * @return An Object TypeMessage whose id is equals to the given id. If
     *         no TypeMessage is found, this method returns null.
     */
    @Query("select e from TypeMessage e where e.id = :id and e.isDeleted = :isDeleted")
    TypeMessage findOne(@Param("id")Integer id, @Param("isDeleted")Boolean isDeleted);

    /**
     * Finds TypeMessage by using libelle as a search criteria.
     *
     * @param libelle
     * @return An Object TypeMessage whose libelle is equals to the given libelle. If
     *         no TypeMessage is found, this method returns null.
     */
    @Query("select e from TypeMessage e where e.libelle = :libelle and e.isDeleted = :isDeleted")
    TypeMessage findByLibelle(@Param("libelle")String libelle, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds TypeMessage by using code as a search criteria.
     *
     * @param code
     * @return An Object TypeMessage whose code is equals to the given code. If
     *         no TypeMessage is found, this method returns null.
     */
    @Query("select e from TypeMessage e where e.code = :code and e.isDeleted = :isDeleted")
    TypeMessage findByCode(@Param("code")String code, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds TypeMessage by using createdAt as a search criteria.
     *
     * @param createdAt
     * @return An Object TypeMessage whose createdAt is equals to the given createdAt. If
     *         no TypeMessage is found, this method returns null.
     */
    @Query("select e from TypeMessage e where e.createdAt = :createdAt and e.isDeleted = :isDeleted")
    List<TypeMessage> findByCreatedAt(@Param("createdAt")Date createdAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds TypeMessage by using updatedAt as a search criteria.
     *
     * @param updatedAt
     * @return An Object TypeMessage whose updatedAt is equals to the given updatedAt. If
     *         no TypeMessage is found, this method returns null.
     */
    @Query("select e from TypeMessage e where e.updatedAt = :updatedAt and e.isDeleted = :isDeleted")
    List<TypeMessage> findByUpdatedAt(@Param("updatedAt")Date updatedAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds TypeMessage by using deletedAt as a search criteria.
     *
     * @param deletedAt
     * @return An Object TypeMessage whose deletedAt is equals to the given deletedAt. If
     *         no TypeMessage is found, this method returns null.
     */
    @Query("select e from TypeMessage e where e.deletedAt = :deletedAt and e.isDeleted = :isDeleted")
    List<TypeMessage> findByDeletedAt(@Param("deletedAt")Date deletedAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds TypeMessage by using createdBy as a search criteria.
     *
     * @param createdBy
     * @return An Object TypeMessage whose createdBy is equals to the given createdBy. If
     *         no TypeMessage is found, this method returns null.
     */
    @Query("select e from TypeMessage e where e.createdBy = :createdBy and e.isDeleted = :isDeleted")
    List<TypeMessage> findByCreatedBy(@Param("createdBy")Integer createdBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds TypeMessage by using updatedBy as a search criteria.
     *
     * @param updatedBy
     * @return An Object TypeMessage whose updatedBy is equals to the given updatedBy. If
     *         no TypeMessage is found, this method returns null.
     */
    @Query("select e from TypeMessage e where e.updatedBy = :updatedBy and e.isDeleted = :isDeleted")
    List<TypeMessage> findByUpdatedBy(@Param("updatedBy")Integer updatedBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds TypeMessage by using deletedBy as a search criteria.
     *
     * @param deletedBy
     * @return An Object TypeMessage whose deletedBy is equals to the given deletedBy. If
     *         no TypeMessage is found, this method returns null.
     */
    @Query("select e from TypeMessage e where e.deletedBy = :deletedBy and e.isDeleted = :isDeleted")
    List<TypeMessage> findByDeletedBy(@Param("deletedBy")Integer deletedBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds TypeMessage by using isDeleted as a search criteria.
     *
     * @param isDeleted
     * @return An Object TypeMessage whose isDeleted is equals to the given isDeleted. If
     *         no TypeMessage is found, this method returns null.
     */
    @Query("select e from TypeMessage e where e.isDeleted = :isDeleted")
    List<TypeMessage> findByIsDeleted(@Param("isDeleted")Boolean isDeleted);



    /**
     * Finds List of TypeMessage by using typeMessageDto as a search criteria.
     *
     * @param request, em
     * @return A List of TypeMessage
     * @throws DataAccessException,ParseException
     */
    public default List<TypeMessage> getByCriteria(Request<TypeMessageDto> request, EntityManager em, Locale locale) throws DataAccessException, Exception {
        String req = "select e from TypeMessage e where e IS NOT NULL";
        HashMap<String, Object> param = new HashMap<String, Object>();
        req += getWhereExpression(request, param, locale);
                TypedQuery<TypeMessage> query = em.createQuery(req, TypeMessage.class);
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
     * Finds count of TypeMessage by using typeMessageDto as a search criteria.
     *
     * @param request, em
     * @return Number of TypeMessage
     *
     */
    public default Long count(Request<TypeMessageDto> request, EntityManager em, Locale locale) throws DataAccessException, Exception  {
        String req = "select count(e.id) from TypeMessage e where e IS NOT NULL";
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
    default String getWhereExpression(Request<TypeMessageDto> request, HashMap<String, java.lang.Object> param, Locale locale) throws Exception {
        // main query
        TypeMessageDto dto = request.getData() != null ? request.getData() : new TypeMessageDto();
        dto.setIsDeleted(false);
        String mainReq = generateCriteria(dto, param, 0, locale);
        // others query
        String othersReq = "";
        if (request.getDatas() != null && !request.getDatas().isEmpty()) {
            Integer index = 1;
            for (TypeMessageDto elt : request.getDatas()) {
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
    default String generateCriteria(TypeMessageDto dto, HashMap<String, Object> param, Integer index,  Locale locale) throws Exception{
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
