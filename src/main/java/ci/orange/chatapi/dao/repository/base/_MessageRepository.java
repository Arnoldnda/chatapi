
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
 * Repository customize : Message.
 *
 * @author Geo
 *
 */
@Repository
public interface _MessageRepository {
	    /**
     * Finds Message by using id as a search criteria.
     *
     * @param id
     * @return An Object Message whose id is equals to the given id. If
     *         no Message is found, this method returns null.
     */
    @Query("select e from Message e where e.id = :id and e.isDeleted = :isDeleted")
    Message findOne(@Param("id")Integer id, @Param("isDeleted")Boolean isDeleted);

    /**
     * Finds Message by using content as a search criteria.
     *
     * @param content
     * @return An Object Message whose content is equals to the given content. If
     *         no Message is found, this method returns null.
     */
    @Query("select e from Message e where e.content = :content and e.isDeleted = :isDeleted")
    List<Message> findByContent(@Param("content")String content, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds Message by using imgUrl as a search criteria.
     *
     * @param imgUrl
     * @return An Object Message whose imgUrl is equals to the given imgUrl. If
     *         no Message is found, this method returns null.
     */
    @Query("select e from Message e where e.imgUrl = :imgUrl and e.isDeleted = :isDeleted")
    List<Message> findByImgUrl(@Param("imgUrl")String imgUrl, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds Message by using createdAt as a search criteria.
     *
     * @param createdAt
     * @return An Object Message whose createdAt is equals to the given createdAt. If
     *         no Message is found, this method returns null.
     */
    @Query("select e from Message e where e.createdAt = :createdAt and e.isDeleted = :isDeleted")
    List<Message> findByCreatedAt(@Param("createdAt")Date createdAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds Message by using updatedAt as a search criteria.
     *
     * @param updatedAt
     * @return An Object Message whose updatedAt is equals to the given updatedAt. If
     *         no Message is found, this method returns null.
     */
    @Query("select e from Message e where e.updatedAt = :updatedAt and e.isDeleted = :isDeleted")
    List<Message> findByUpdatedAt(@Param("updatedAt")Date updatedAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds Message by using deletedAt as a search criteria.
     *
     * @param deletedAt
     * @return An Object Message whose deletedAt is equals to the given deletedAt. If
     *         no Message is found, this method returns null.
     */
    @Query("select e from Message e where e.deletedAt = :deletedAt and e.isDeleted = :isDeleted")
    List<Message> findByDeletedAt(@Param("deletedAt")Date deletedAt, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds Message by using createdBy as a search criteria.
     *
     * @param createdBy
     * @return An Object Message whose createdBy is equals to the given createdBy. If
     *         no Message is found, this method returns null.
     */
    @Query("select e from Message e where e.createdBy = :createdBy and e.isDeleted = :isDeleted")
    List<Message> findByCreatedBy(@Param("createdBy")Integer createdBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds Message by using updatedBy as a search criteria.
     *
     * @param updatedBy
     * @return An Object Message whose updatedBy is equals to the given updatedBy. If
     *         no Message is found, this method returns null.
     */
    @Query("select e from Message e where e.updatedBy = :updatedBy and e.isDeleted = :isDeleted")
    List<Message> findByUpdatedBy(@Param("updatedBy")Integer updatedBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds Message by using deletedBy as a search criteria.
     *
     * @param deletedBy
     * @return An Object Message whose deletedBy is equals to the given deletedBy. If
     *         no Message is found, this method returns null.
     */
    @Query("select e from Message e where e.deletedBy = :deletedBy and e.isDeleted = :isDeleted")
    List<Message> findByDeletedBy(@Param("deletedBy")Integer deletedBy, @Param("isDeleted")Boolean isDeleted);
    /**
     * Finds Message by using isDeleted as a search criteria.
     *
     * @param isDeleted
     * @return An Object Message whose isDeleted is equals to the given isDeleted. If
     *         no Message is found, this method returns null.
     */
    @Query("select e from Message e where e.isDeleted = :isDeleted")
    List<Message> findByIsDeleted(@Param("isDeleted")Boolean isDeleted);

    /**
     * Finds Message by using conversationId as a search criteria.
     *
     * @param conversationId
     * @return An Object Message whose conversationId is equals to the given conversationId. If
     *         no Message is found, this method returns null.
     */
    @Query("select e from Message e where e.conversation.id = :conversationId and e.isDeleted = :isDeleted")
    List<Message> findByConversationId(@Param("conversationId")Integer conversationId, @Param("isDeleted")Boolean isDeleted);

  /**
   * Finds one Message by using conversationId as a search criteria.
   *
   * @param conversationId
   * @return An Object Message whose conversationId is equals to the given conversationId. If
   *         no Message is found, this method returns null.
   */
  @Query("select e from Message e where e.conversation.id = :conversationId and e.isDeleted = :isDeleted")
  Message findMessageByConversationId(@Param("conversationId")Integer conversationId, @Param("isDeleted")Boolean isDeleted);


    /**
     * Finds Message by using typeMessage as a search criteria.
     *
     * @param typeMessage
     * @return An Object Message whose typeMessage is equals to the given typeMessage. If
     *         no Message is found, this method returns null.
     */
    @Query("select e from Message e where e.typeMessage2.id = :typeMessage and e.isDeleted = :isDeleted")
    List<Message> findByTypeMessage(@Param("typeMessage")Integer typeMessage, @Param("isDeleted")Boolean isDeleted);

  /**
   * Finds one Message by using typeMessage as a search criteria.
   *
   * @param typeMessage
   * @return An Object Message whose typeMessage is equals to the given typeMessage. If
   *         no Message is found, this method returns null.
   */
  @Query("select e from Message e where e.typeMessage2.id = :typeMessage and e.isDeleted = :isDeleted")
  Message findMessageByTypeMessage(@Param("typeMessage")Integer typeMessage, @Param("isDeleted")Boolean isDeleted);




    /**
     * Finds List of Message by using messageDto as a search criteria.
     *
     * @param request, em
     * @return A List of Message
     * @throws DataAccessException,ParseException
     */
    public default List<Message> getByCriteria(Request<MessageDto> request, EntityManager em, Locale locale) throws DataAccessException, Exception {
        String req = "select e from Message e where e IS NOT NULL";
        HashMap<String, Object> param = new HashMap<String, Object>();
        req += getWhereExpression(request, param, locale);
                TypedQuery<Message> query = em.createQuery(req, Message.class);
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
     * Finds count of Message by using messageDto as a search criteria.
     *
     * @param request, em
     * @return Number of Message
     *
     */
    public default Long count(Request<MessageDto> request, EntityManager em, Locale locale) throws DataAccessException, Exception  {
        String req = "select count(e.id) from Message e where e IS NOT NULL";
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
    default String getWhereExpression(Request<MessageDto> request, HashMap<String, java.lang.Object> param, Locale locale) throws Exception {
        // main query
        MessageDto dto = request.getData() != null ? request.getData() : new MessageDto();
        dto.setIsDeleted(false);
        String mainReq = generateCriteria(dto, param, 0, locale);
        // others query
        String othersReq = "";
        if (request.getDatas() != null && !request.getDatas().isEmpty()) {
            Integer index = 1;
            for (MessageDto elt : request.getDatas()) {
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
    default String generateCriteria(MessageDto dto, HashMap<String, Object> param, Integer index,  Locale locale) throws Exception{
        List<String> listOfQuery = new ArrayList<String>();
        if (dto != null) {
            if (dto.getId() != null || Utilities.searchParamIsNotEmpty(dto.getIdParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("id", dto.getId(), "e.id", "Integer", dto.getIdParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getContent()) || Utilities.searchParamIsNotEmpty(dto.getContentParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("content", dto.getContent(), "e.content", "String", dto.getContentParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getImgUrl()) || Utilities.searchParamIsNotEmpty(dto.getImgUrlParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("imgUrl", dto.getImgUrl(), "e.imgUrl", "String", dto.getImgUrlParam(), param, index, locale));
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
                        if (dto.getConversationId() != null || Utilities.searchParamIsNotEmpty(dto.getConversationIdParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("conversationId", dto.getConversationId(), "e.conversation.id", "Integer", dto.getConversationIdParam(), param, index, locale));
            }
                        if (dto.getTypeMessage() != null || Utilities.searchParamIsNotEmpty(dto.getTypeMessageParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("typeMessage", dto.getTypeMessage(), "e.typeMessage2.id", "Integer", dto.getTypeMessageParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getTypeMessageLibelle()) || Utilities.searchParamIsNotEmpty(dto.getTypeMessageLibelleParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("typeMessageLibelle", dto.getTypeMessageLibelle(), "e.typeMessage2.libelle", "String", dto.getTypeMessageLibelleParam(), param, index, locale));
            }
            if (Utilities.isNotBlank(dto.getTypeMessageCode()) || Utilities.searchParamIsNotEmpty(dto.getTypeMessageCodeParam())) {
                listOfQuery.add(CriteriaUtils.generateCriteria("typeMessageCode", dto.getTypeMessageCode(), "e.typeMessage2.code", "String", dto.getTypeMessageCodeParam(), param, index, locale));
            }

            /*List<String> listOfCustomQuery = _generateCriteria(dto, param, index, locale);
            if (Utilities.isNotEmpty(listOfCustomQuery)) {
                listOfQuery.addAll(listOfCustomQuery);
            }*/
        }
        return CriteriaUtils.getCriteriaByListOfQuery(listOfQuery);
    }
}
