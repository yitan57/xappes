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

@DynamoDBTable(tableName = "xappes-mobilehub-1241857271-Xapes")

public class XapesDO {
    private String _xapesId;
    private String _cavaName;
    private String _cavaPlace;
    private String _userId;
    private String _xappa;

    @DynamoDBHashKey(attributeName = "xapesId")
    @DynamoDBAttribute(attributeName = "xapesId")
    public String getXapesId() {
        return _xapesId;
    }

    public void setXapesId(final String _xapesId) {
        this._xapesId = _xapesId;
    }
    @DynamoDBAttribute(attributeName = "cavaName")
    public String getCavaName() {
        return _cavaName;
    }

    public void setCavaName(final String _cavaName) {
        this._cavaName = _cavaName;
    }
    @DynamoDBAttribute(attributeName = "cavaPlace")
    public String getCavaPlace() {
        return _cavaPlace;
    }

    public void setCavaPlace(final String _cavaPlace) {
        this._cavaPlace = _cavaPlace;
    }
    @DynamoDBAttribute(attributeName = "userId")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }
    @DynamoDBAttribute(attributeName = "xappa")
    public String getXappa() {
        return _xappa;
    }

    public void setXappa(final String _xappa) {
        this._xappa = _xappa;
    }
}
