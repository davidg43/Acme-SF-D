
package acme.entities.trainingModule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrainingSession extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "TS-[A-Z]{1,3}-[0-9]{3}")
	private String				code;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				iniDate;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				finalDate;

	@NotBlank
	@Length(max = 75)
	private String				location;

	@NotBlank
	@Length(max = 75)
	private String				instructor;

	@NotNull
	@Email
	private String				contactEmail;

	@URL
	private String				link;


	@Transient
	public Date period() {
		if (this.iniDate != null && this.finalDate != null && this.finalDate.after(this.iniDate)) {
			long diffInMillies = Math.abs(this.finalDate.getTime() - this.iniDate.getTime());
			return new Date(diffInMillies);
		}
		return null;
	}

	//-----


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private TrainingModule trainingModule;

}
