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

@DynamoDBTable(tableName = "xappes-mobilehub-1241857271-Entradas")

public class EntradasDO {
    private String _entradaId;
    private String _entradaType;
    private String _entrada;

    @DynamoDBHashKey(attributeName = "entradaId")
    @DynamoDBAttribute(attributeName = "entradaId")
    public String getEntradaId() {
        return _entradaId;
    }

    public void setEntradaId(final String _entradaId) {
        this._entradaId = _entradaId;
    }
    @DynamoDBRangeKey(attributeName = "entradaType")
    @DynamoDBAttribute(attributeName = "entradaType")
    public String getEntradaType() {
        return _entradaType;
    }

    public void setEntradaType(final String _entradaType) {
        this._entradaType = _entradaType;
    }
    @DynamoDBAttribute(attributeName = "entrada")
    public String getEntrada() {
        return _entrada;
    }

    public void setEntrada(final String _entrada) {
        this._entrada = _entrada;
    }

}
