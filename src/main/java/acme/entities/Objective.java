
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Objective extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				instantiationMoment;

	@NotBlank
	@Length(max = 75)
	private String				title;

	@NotBlank
	@Length(max = 100)
	private String				description;

	@NotNull
	private Priority			priority;

	private boolean				criticalStatus;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				duration;

	@URL
	private String				link;


	@AssertTrue(message = "La duracion del objetivo debe comenzar una vez establecido el objetivo")
	public boolean isUpdateMomentAfterCreationMoment() {
		return this.instantiationMoment != null && this.duration != null && this.duration.after(this.instantiationMoment);
	}


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Project project;

}