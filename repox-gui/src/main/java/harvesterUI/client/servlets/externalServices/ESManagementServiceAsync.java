package harvesterUI.client.servlets.externalServices;

import com.google.gwt.user.client.rpc.AsyncCallback;
import harvesterUI.shared.externalServices.ExternalServiceUI;

import java.util.List;

public interface ESManagementServiceAsync {

    public void removeExternalService(List<ExternalServiceUI> externalServiceUIs, AsyncCallback<String> callback);

    public void saveExternalService(boolean isUpdate, ExternalServiceUI externalServiceUI, AsyncCallback<String> callback);

    public void getAllExternalServices(boolean checkStatus, AsyncCallback<List<ExternalServiceUI>> callback);

}
