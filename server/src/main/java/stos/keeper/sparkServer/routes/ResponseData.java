package stos.keeper.sparkServer.routes;

class ResponseData {
    private int responseStatus;
    private String responseMessage;

    public ResponseData(int status, String message) {
        this.responseStatus = status;
        this.responseMessage = message;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public String getResponseMessage() {
        return responseMessage;
    }
}
