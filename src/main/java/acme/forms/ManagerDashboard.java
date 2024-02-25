
package acme.forms;

import javax.validation.constraints.PositiveOrZero;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	private int					totalMustUserStories;

	private int					totalShouldUserStories;

	private int					totalCouldUserStories;

	private int					totalWontUserStories;

	@PositiveOrZero
	private double				averageEstimatedCostOfUserStories;

	@PositiveOrZero
	private double				deviationOfEstimatedCostOfUserStories;

	@PositiveOrZero
	private int					minimumEstimatedCostOfUserStories;

	@PositiveOrZero
	private int					maximumEstimatedCostOfUserStories;

	@PositiveOrZero
	private double				averageCostOfProjects;

	@PositiveOrZero
	private double				deviationOfCostOfProjects;

	@PositiveOrZero
	private int					minimumCostOfProjects;

	@PositiveOrZero
	private int					maximumCostOfProjects;

}