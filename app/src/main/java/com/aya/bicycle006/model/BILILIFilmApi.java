package com.aya.bicycle006.model;

import java.util.List;

/**
 * Created by Single on 2016/3/4.
 */
public class BILILIFilmApi {

    /**
     * note : 统计所有投稿在 2016年02月25日 - 2016年03月03日 的数据综合得分，每日更新一次
     * code : 0
     * pages : 1
     * num : 100
     * list : []
     */

    private RankEntity rank;

    public void setRank(RankEntity rank) {
        this.rank = rank;
    }

    public RankEntity getRank() {
        return rank;
    }

    public static class RankEntity {
        private String note;
        private int code;
        private int pages;
        private int num;
        private List<BILILIFilm> list;

        public void setNote(String note) {
            this.note = note;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public void setList(List<BILILIFilm> list) {
            this.list = list;
        }

        public String getNote() {
            return note;
        }

        public int getCode() {
            return code;
        }

        public int getPages() {
            return pages;
        }

        public int getNum() {
            return num;
        }

        public List<BILILIFilm> getList() {
            return list;
        }
    }
}
