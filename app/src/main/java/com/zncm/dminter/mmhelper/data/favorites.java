package com.zncm.dminter.mmhelper.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.query.In;

/**
 * DROP TABLE IF EXISTS "main"."favorites";
 * CREATE TABLE favorites (_id INTEGER PRIMARY KEY,title TEXT,intent TEXT,container INTEGER,screen INTEGER,cellX INTEGER,cellY INTEGER,spanX INTEGER,spanY INTEGER,itemType INTEGER,
 * appWidgetId INTEGER NOT NULL DEFAULT -1,
 * iconPackage TEXT,iconResource TEXT,icon BLOB,customIcon BLOB,titleAlias TEXT,appWidgetProvider TEXT,modified INTEGER NOT NULL DEFAULT 0,
 * restored INTEGER NOT NULL DEFAULT 0,profileId INTEGER DEFAULT 0,
 * rank INTEGER NOT NULL DEFAULT 0,options INTEGER NOT NULL DEFAULT 0);
 */

public class favorites {
  
    private Integer _id;
  
    private String title;
  
    private String intent;
  
    private Integer container;
  
    private Integer screen;
  
    private Integer cellX;
  
    private Integer cellY;
  
    private Integer spanX;
  
    private Integer spanY;
  
    private Integer itemType;
  
    private Integer appWidgetId;
  
    private String iconPackage;
  
    private String iconResource;
  
    private byte[]  icon;
  
    private byte[]  customIcon;
  
    private String titleAlias;
  
    private String appWidgetProvider;
  
    private Integer modified;


    public favorites() {
    }
    public favorites(String title, String intent, String iconPackage) {
        this.title = title;
        this.intent = intent;
        this.iconPackage = iconPackage;
    }
    public favorites(String title, String intent, String iconPackage, byte[] icon) {
        this.title = title;
        this.intent = intent;
        this.iconPackage = iconPackage;
        this.icon = icon;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public Integer getContainer() {
        return container;
    }

    public void setContainer(Integer container) {
        this.container = container;
    }

    public Integer getScreen() {
        return screen;
    }

    public void setScreen(Integer screen) {
        this.screen = screen;
    }

    public Integer getCellX() {
        return cellX;
    }

    public void setCellX(Integer cellX) {
        this.cellX = cellX;
    }

    public Integer getCellY() {
        return cellY;
    }

    public void setCellY(Integer cellY) {
        this.cellY = cellY;
    }

    public Integer getSpanX() {
        return spanX;
    }

    public void setSpanX(Integer spanX) {
        this.spanX = spanX;
    }

    public Integer getSpanY() {
        return spanY;
    }

    public void setSpanY(Integer spanY) {
        this.spanY = spanY;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getAppWidgetId() {
        return appWidgetId;
    }

    public void setAppWidgetId(Integer appWidgetId) {
        this.appWidgetId = appWidgetId;
    }

    public String getIconPackage() {
        return iconPackage;
    }

    public void setIconPackage(String iconPackage) {
        this.iconPackage = iconPackage;
    }

    public String getIconResource() {
        return iconResource;
    }

    public void setIconResource(String iconResource) {
        this.iconResource = iconResource;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public byte[] getCustomIcon() {
        return customIcon;
    }

    public void setCustomIcon(byte[] customIcon) {
        this.customIcon = customIcon;
    }

    public String getTitleAlias() {
        return titleAlias;
    }

    public void setTitleAlias(String titleAlias) {
        this.titleAlias = titleAlias;
    }

    public String getAppWidgetProvider() {
        return appWidgetProvider;
    }

    public void setAppWidgetProvider(String appWidgetProvider) {
        this.appWidgetProvider = appWidgetProvider;
    }

    public Integer getModified() {
        return modified;
    }

    public void setModified(Integer modified) {
        this.modified = modified;
    }
}
