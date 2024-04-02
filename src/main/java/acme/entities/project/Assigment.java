
package acme.entities.project;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Assigment extends AbstractEntity {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Valid
	@ManyToOne(optional = false)
	@NotNull
	private Project				project;

	@Valid
	@ManyToOne(optional = false)
	@NotNull
	private UserStory			userStory;


	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || this.getClass() != o.getClass())
			return false;
		Assigment assigment = (Assigment) o;
		return Objects.equals(this.userStory, assigment.userStory) && Objects.equals(this.project, assigment.project);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.userStory, this.project);
	}
}
