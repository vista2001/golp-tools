package dev.db.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;

import dev.db.pojo.TServer;

public interface EditorsServerService
{
    public Map<String,String> queryServerByIdOrName(String key,int type,PreferenceStore ps) throws SQLException;
    
    public void updateServerById(List<String> datalist,PreferenceStore ps) throws SQLException;
    
    public void insertServer(TServer server, String prjId) throws SQLException;
            
}
