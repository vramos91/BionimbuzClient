package br.unb.cic.bionimbuz.rest.response;

public class DeleteFileResponse implements ResponseInfo {

    private boolean deleted;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
