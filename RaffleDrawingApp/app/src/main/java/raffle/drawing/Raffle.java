package raffle.drawing;


import android.os.Parcelable;

import java.io.Serializable;

public class Raffle implements Serializable {

    private int id;
    private String title;
    private String description;
    private RaffleType type;
    private RaffleStatus status;
    private int ticketNumber;
    private int soldTickets;
    private int winNumber;
    private String Date;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public RaffleType getType() {
        return type;
    }

    public void setType(RaffleType type) {
        this.type = type;
    }

    public void setStatus(RaffleStatus status) {
        this.status = status;
    }

    public RaffleStatus getStatus() {
        return status;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public int getSoldTickets() {
        return soldTickets;
    }

    public void setSoldTickets(int soldTickets) {
        this.soldTickets = soldTickets;
    }

    public int getWinNumber() {
        return winNumber;
    }

    public void setWinNumber(int winNumber) {
        this.winNumber = winNumber;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}

