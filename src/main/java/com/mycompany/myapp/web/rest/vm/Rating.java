package com.mycompany.myapp.web.rest.vm;

/**
 * Created by administrator on 12/15/16.
 */

public class Rating implements Comparable<Rating> {
    public double sumRatings = 0;
    public double count = 0;

    public void Add(int rating)
    {
        sumRatings+=rating;
        count++;
    }

    public double GetAverage()
    {
        return sumRatings/count;
    }

    @Override
    public int compareTo(Rating o) {

        Rating secondRating = (Rating)o;
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if (this == secondRating) return EQUAL;

        if(this.GetAverage() < secondRating.GetAverage())
            return BEFORE;
        else if (this.GetAverage() > secondRating.GetAverage())
            return AFTER;
        else
            return EQUAL;
    }
}
