
/*
 * Created on 2026-01-03 ( Time 10:00:17 )
 * Generator tool : Telosys Tools Generator ( version 3.3.0 )
 * Copyright 2018 Geo. All Rights Reserved.
 */

package ci.orange.chatapi.utils.contract;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * Search Param
 * 
 * @author Geo
 *
 */
@Data
@ToString
@NoArgsConstructor
public class SearchParam<T> {
	String  operator;
    T       start;
    T       end;
    List<T> datas;
    Float   distance;

    public SearchParam(String operator, T start, T end, List<T> datas, Float distance) {
        super();
        this.operator = operator;
        this.start    = start;
        this.end      = end;
        this.datas    = datas;
        this.distance = distance;
    }

    public SearchParam(String operator, T start, T end, List<T> datas) {
        this(operator, start, end, datas, null);
    }

    public SearchParam(String operator) {
        this(operator, null, null, null);
    }

    public SearchParam(String operator, T start, T end) {
        this(operator, start, end, null);
    }

    public SearchParam(String operator, List<T> datas) {
        this(operator, null, null, datas);
    }

    public SearchParam(String operator, Float distance) {
        this(operator, null, null, null, distance);
    }
}
