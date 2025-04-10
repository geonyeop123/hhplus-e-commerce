package kr.hhplus.be.server.domain.product;

public record ProductCommand(
        int pageNo,
        int pageSize
) {
    int getOffset(){
        return (pageNo - 1) * pageSize;
    }
}
