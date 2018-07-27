package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "xappes-mobilehub-1241857271-Trobades")

public class TrobadesDO implements Serializable {

    private String _trobadaId;
    private String _tipoMarca;
    private String _trobadaLat;
    private String _trobadaLon;
    private String _trobadaText;
    private String _trobadaTitle;
    private String _trobadaData;

    private static final long serialVersionUID = -5199808308197336694L;

    @DynamoDBHashKey(attributeName = "trobadaId")
    @DynamoDBAttribute(attributeName = "trobadaId")
    public String getTrobadaId() {
        return _trobadaId;
    }

    public void setTrobadaId(final String _trobadaId) {
        this._trobadaId = _trobadaId;
    }
    @DynamoDBAttribute(attributeName = "tipoMarca")
    public String getTipoMarca() {
        return _tipoMarca;
    }

    public void setTipoMarca(final String _tipoMarca) {
        this._tipoMarca = _tipoMarca;
    }
    @DynamoDBAttribute(attributeName = "trobadaLat")
    public String getTrobadaLat() {
        return _trobadaLat;
    }

    public void setTrobadaLat(final String _trobadaLat) {
        this._trobadaLat = _trobadaLat;
    }
    @DynamoDBAttribute(attributeName = "trobadaLon")
    public String getTrobadaLon() {
        return _trobadaLon;
    }

    public void setTrobadaLon(final String _trobadaLon) {
        this._trobadaLon = _trobadaLon;
    }
    @DynamoDBAttribute(attributeName = "trobadaText")
    public String getTrobadaText() {
        return _trobadaText;
    }

    public void setTrobadaText(final String _trobadaText) {
        this._trobadaText = _trobadaText;
    }
    @DynamoDBAttribute(attributeName = "trobadaTitle")
    public String getTrobadaTitle() {
        return _trobadaTitle;
    }

    public void setTrobadaTitle(final String _trobadaTitle) {
        this._trobadaTitle = _trobadaTitle;
    }
    @DynamoDBAttribute(attributeName = "_trobadaData")
    public String getTrobadaData() {
        return _trobadaData;
    }

    public void setTrobadaData(final String _trobadaData) {
        this._trobadaData = _trobadaData;
    }
}
