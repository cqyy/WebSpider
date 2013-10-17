package cn.edu.uestc.wsc.Cache;

/*缓存操作接口*/
public interface Cache {
  int  getValue(String key);
  void setValue(String key,int num);
  void del(String key);
}
