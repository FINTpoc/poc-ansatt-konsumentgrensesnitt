package no.fk.fint.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String orgId;
    private String logo;

    public User(String orgId) {
        this.orgId = orgId;
    }
}
