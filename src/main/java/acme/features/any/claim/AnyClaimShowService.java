/*
 * ClaimShowService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.any.claim;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Anonymous;
import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.Claim;

@Service
public class AnyClaimShowService extends AbstractService<Any, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyClaimRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Claim claim;

		masterId = super.getRequest().getData("id", int.class);
		claim = this.repository.findOneClaimById(masterId);
		status = !super.getRequest().getPrincipal().hasRole(Anonymous.class) && claim != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneClaimById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Claim object) {
		assert object != null;

		boolean confirm = object.isConfirm() == true;
		Dataset dataset;

		dataset = super.unbind(object, "code", "instantiationMoment", "heading", "description", "department", "emailAddress", "link", "isDraft", "confirm");

		dataset.put("confirm", confirm);
		super.getResponse().addData(dataset);
	}

}
