package surfid.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MagicLinkRequest {

    @NotNull
    private String authenticationRequestId;

    @NotNull
    private User user;

}