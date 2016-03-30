package com.aya.bicycle006.model;

import java.util.List;

/**
 * Created by Single on 2016/3/28.
 */
public class GankData  {
    
    /**
     * category : ["iOS","前端","Android","休息视频","瞎推荐","App","拓展资源","福利"]
     * error : false
     * results : {"Android":[],"App":[],"iOS":[],"休息视频":[],"前端":[],"拓展资源":[],"瞎推荐":[],"福利":[]}
     */
    
    private boolean error;
    private ResultsEntity results;
    private List<String> category;
    
    public boolean isError() {
        return error;
    }
    
    public void setError(boolean error) {
        this.error = error;
    }
    
    public ResultsEntity getResults() {
        return results;
    }
    
    public void setResults(ResultsEntity results) {
        this.results = results;
    }
    
    public List<String> getCategory() {
        return category;
    }
    
    public void setCategory(List<String> category) {
        this.category = category;
    }
    
    public static class ResultsEntity {
        private List<Gank> Android;
        private List<Gank> App;
        private List<Gank> iOS;
        private List<Gank> 休息视频;
        private List<Gank> 前端;
        private List<Gank> 拓展资源;
        private List<Gank> 瞎推荐;
        private List<Gank> 福利;
        
        public List<Gank> getAndroid() {
            return Android;
        }
        
        public void setAndroid(List<Gank> Android) {
            this.Android = Android;
        }
        
        public List<Gank> getApp() {
            return App;
        }
        
        public void setApp(List<Gank> App) {
            this.App = App;
        }
        
        public List<Gank> getIOS() {
            return iOS;
        }
        
        public void setIOS(List<Gank> iOS) {
            this.iOS = iOS;
        }
        
        public List<Gank> get休息视频() {
            return 休息视频;
        }
        
        public void set休息视频(List<Gank> 休息视频) {
            this.休息视频 = 休息视频;
        }
        
        public List<Gank> get前端() {
            return 前端;
        }
        
        public void set前端(List<Gank> 前端) {
            this.前端 = 前端;
        }
        
        public List<Gank> get拓展资源() {
            return 拓展资源;
        }
        
        public void set拓展资源(List<Gank> 拓展资源) {
            this.拓展资源 = 拓展资源;
        }
        
        public List<Gank> get瞎推荐() {
            return 瞎推荐;
        }
        
        public void set瞎推荐(List<Gank> 瞎推荐) {
            this.瞎推荐 = 瞎推荐;
        }
        
        public List<Gank> get福利() {
            return 福利;
        }
        
        public void set福利(List<Gank> 福利) {
            this.福利 = 福利;
        }
    }
}
