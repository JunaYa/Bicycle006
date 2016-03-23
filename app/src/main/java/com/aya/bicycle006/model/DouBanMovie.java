package com.aya.bicycle006.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Single on 2016/3/22.
 */
public class DouBanMovie {

    @SerializedName("count") public int count;
    @SerializedName("start") public int start;
    @SerializedName("total") public int total;
    @SerializedName("subjects") public List<Subjects> subjects;
    @SerializedName("title") public String title;

    public class Subjects {
        @SerializedName("rating") public Rating rating;

        public class Rating {
            @SerializedName("max") public int max;
            @SerializedName("min") public int min;
            @SerializedName("stars") public String stars;
            @SerializedName("average") public float average;
        }

        //        @SerializedName("genres") public String[] genres;
        @SerializedName("title") public String title;
        @SerializedName("casts") public List<Casts> casts;

        public class Casts {
            @SerializedName("alt") public String alt;
            @SerializedName("avatars") public Avatars avatars;

            public class Avatars {
                @SerializedName("small") public String small;
                @SerializedName("large") public String large;
                @SerializedName("medium") public String medium;
            }

            @SerializedName("name") public String name;
            @SerializedName("id") public int id;
        }

        @SerializedName("collect_count") public int collect_count;
        @SerializedName("original_title") public String original_title;
        @SerializedName("subtype") public String subtype;
        @SerializedName("year") public int year;
        @SerializedName("alt") public String alt;
        @SerializedName("id") public int id;
        @SerializedName("images") public Images images;

        public class Images {
            @SerializedName("small") public String small;
            @SerializedName("large") public String large;
            @SerializedName("medium") public String medium;
        }

    }
}
