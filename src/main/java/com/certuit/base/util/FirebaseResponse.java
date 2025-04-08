package com.certuit.base.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class FirebaseResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("multicast_id")
    private Object multicastId;

    @JsonProperty("success")
    private Integer success;

    @JsonProperty("failure")
    private Integer failure;

    @JsonProperty("canonical_ids")
    private Integer canonicalIds;

    @JsonProperty("results")
    private List<Object> results;

    public FirebaseResponse() {

    }

    @Override
    public String toString() {
        return "FirebaseResponse [multicastId=" + multicastId + ", success=" + success + ", failure=" + failure
                + ", canonicalIds=" + canonicalIds + ", results=" + results + "]";
    }
}
