package ApiEntity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kartikeya on 20/10/2018.
 */

public class CreateAgendaEntity {
    @SerializedName("userID")
    String userID;
    @SerializedName("agendaID")
    String agendaID;
     @SerializedName("agenda_text")
    String agenda_text;
    @SerializedName("agenda_date")
    String agenda_date;
    @SerializedName("agenda_start_time")
    String agenda_start_time;
    @SerializedName("agenda_end_time")
    String agenda_end_time;
    @SerializedName("agenda_type")
    String agenda_type;

    public CreateAgendaEntity(String userID, String agenda_text, String agenda_date, String agenda_start_time,
                              String agenda_end_time, String agenda_type) {
        this.userID = userID;
        this.agenda_text = agenda_text;
        this.agenda_date = agenda_date;
        this.agenda_start_time = agenda_start_time;
        this.agenda_end_time = agenda_end_time;
        this.agenda_type = agenda_type;
    }

    public CreateAgendaEntity(String userID, String agendaID, String agenda_text, String agenda_date,
                              String agenda_start_time, String agenda_end_time, String agenda_type) {
        this.userID = userID;
        this.agendaID = agendaID;
        this.agenda_text = agenda_text;
        this.agenda_date = agenda_date;
        this.agenda_start_time = agenda_start_time;
        this.agenda_end_time = agenda_end_time;
        this.agenda_type = agenda_type;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAgenda_text() {
        return agenda_text;
    }

    public void setAgenda_text(String agenda_text) {
        this.agenda_text = agenda_text;
    }

    public String getAgenda_date() {
        return agenda_date;
    }

    public void setAgenda_date(String agenda_date) {
        this.agenda_date = agenda_date;
    }

    public String getAgenda_start_time() {
        return agenda_start_time;
    }

    public void setAgenda_start_time(String agenda_start_time) {
        this.agenda_start_time = agenda_start_time;
    }

    public String getAgenda_end_time() {
        return agenda_end_time;
    }

    public void setAgenda_end_time(String agenda_end_time) {
        this.agenda_end_time = agenda_end_time;
    }

    public String getAgenda_type() {
        return agenda_type;
    }

    public void setAgenda_type(String agenda_type) {
        this.agenda_type = agenda_type;
    }

    public String getAgendaID() {
        return agendaID;
    }

    public void setAgendaID(String agendaID) {
        this.agendaID = agendaID;
    }
}
