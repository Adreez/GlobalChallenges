package sk.adr3ez.globalchallenges.core.model;

public class TimeUtils {


    public static String format(long milliseconds) {
        if (milliseconds < 0) {
            throw new IllegalArgumentException("Milliseconds cannot be negative");
        }

        long seconds = milliseconds / 1000;
        milliseconds %= 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        long hours = minutes / 60;
        minutes %= 60;
        long days = hours / 24;
        hours %= 24;
        long years = days / 365;
        days %= 365;
        long months = days / 30;
        days %= 30;

        StringBuilder result = new StringBuilder();

        if (years > 0) {
            result.append(years).append(" year").append(years > 1 ? "s " : " ");
            if (months > 0) {
                result.append(months).append(" month").append(months > 1 ? "s" : "");
            }
        } else if (months > 0) {
            result.append(months).append(" month").append(months > 1 ? "s " : " ");
            if (days > 0) {
                result.append(days).append(" day").append(days > 1 ? "s" : "");
            }
        } else if (days > 0) {
            result.append(days).append(" day").append(days > 1 ? "s " : " ");
            if (hours > 0) {
                result.append(hours).append(" hour").append(hours > 1 ? "s" : "");
            }
        } else if (hours > 0) {
            result.append(hours).append(" hour").append(hours > 1 ? "s " : " ");
            if (minutes > 0) {
                result.append(minutes).append(" min").append(minutes > 1 ? "s" : "");
            }
        } else if (minutes > 0) {
            result.append(minutes).append(" min").append(minutes > 1 ? "s " : " ");
            if (seconds > 0) {
                result.append(seconds).append(" sec");
            }
        } else if (seconds > 0) {
            result.append(seconds).append(" sec").append(" ");
            if (milliseconds > 0) {
                result.append(milliseconds).append(" ms");
            }
        } else {
            result.append(milliseconds).append(" ms");
        }

        return result.toString().trim();
    }

}
