package com.deskconn.dmr.database;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MainDao {

    @Insert(onConflict = REPLACE)
    void insert(WhatsappData whatsappData);


    @Insert(onConflict = REPLACE)
    void insert(TelegramData telegramData);

    @Insert(onConflict = REPLACE)
    void insert(InstagramData instagramData);

    @Query("SELECT * FROM whatsapp_data")
    List<WhatsappData> getAll();

    @Query("SELECT * FROM telegram_table")
    List<TelegramData> getAllTelegram();

    @Query("SELECT * FROM instagram_table")
    List<InstagramData> getAllInstagram();

    @Query("SELECT messages FROM whatsapp_data where name = :sName")
    String getAllMessages(String sName);

    @Query("SELECT messages FROM telegram_table where name = :sName")
    String getAllMessagesTelegram(String sName);

    @Query("SELECT messages FROM instagram_table where name = :sName")
    String getAllMessagesInstagram(String sName);

    @Query("SELECT time FROM whatsapp_data where name = :sName")
    String getAllTime(String sName);

    @Query("SELECT time FROM telegram_table where name = :sName")
    String getAllTimeTelegram(String sName);

    @Query("SELECT time FROM instagram_table where name = :sName")
    String getAllTimeInstagram(String sName);

    @Query("DELETE FROM whatsapp_data where name = :sName")
    void deleteChat(String sName);

    @Query("DELETE FROM telegram_table where name = :sName")
    void deleteChatTelegram(String sName);

    @Query("DELETE FROM instagram_table where name = :sName")
    void deleteChatInstagram(String sName);


}