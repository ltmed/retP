package com.intellica.retouch.model;

public class ResourceTables {
        private String id;
        private String schema;
        private String table_name;
        private String url;
        private String temp_table_name;
        private String history_table_name;
        private String created_by;
        private String creation_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTemp_table_name() {
        return temp_table_name;
    }

    public void setTemp_table_name(String temp_table_name) {
        this.temp_table_name = temp_table_name;
    }

    public String getHistory_table_name() {
        return history_table_name;
    }

    public void setHistory_table_name(String history_table_name) {
        this.history_table_name = history_table_name;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }
}
