package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "xappes-mobilehub-1241857271-Caves")

public class CavesDO {
    private String _cavaId;
    private String _cavaName;
    private String _numXappes;

    @DynamoDBHashKey(attributeName = "cavaId")
    @DynamoDBAttribute(attributeName = "cavaId")
    public String getCavaId() {
        return _cavaId;
    }

    public void setCavaId(final String _cavaId) {
        this._cavaId = _cavaId;
    }
    @DynamoDBRangeKey(attributeName = "cavaName")
    @DynamoDBAttribute(attributeName = "cavaName")
    public String getCavaName() {
        return _cavaName;
    }

    public void setCavaName(final String _cavaName) {
        this._cavaName = _cavaName;
    }

    @DynamoDBAttribute(attributeName = "numXappes")
    public String getNumXappes() {
        return _numXappes;
    }

    public void setNumXappes(final String _numXappes) {
        this._numXappes = _numXappes;
    }

}
