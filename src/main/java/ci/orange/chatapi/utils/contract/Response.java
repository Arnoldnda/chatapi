
/*
 * Created on 2026-01-03 ( Time 10:00:17 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2018 Geo. All Rights Reserved.
 */

package ci.orange.chatapi.utils.contract;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

/**
 * Response
 * 
 * @author Geo
 *
 */
@Data
@ToString
@NoArgsConstructor
@XmlRootElement
@JsonInclude(Include.NON_NULL)
public class Response<T> extends ResponseBase {

	protected List<T> items;
}
