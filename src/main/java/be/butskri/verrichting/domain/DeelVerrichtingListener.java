package be.butskri.verrichting.domain;

import java.io.Serializable;

public interface DeelVerrichtingListener extends Serializable {

	void statusChanged(Deelverrichting deelVerrichting, VerrichtingStatus oldStatus, VerrichtingStatus newStatus);

}
