/*
 * AuditorAuditRecordPublishService.java
 *
 * Copyright (C) 2012-2024 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.auditor.auditRecord;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.codeAudit.AuditRecord;
import acme.entities.codeAudit.CodeAudit;
import acme.entities.codeAudit.Mark;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordPublishService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuditorAuditRecordRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int auditRecordId;
		CodeAudit codeAudit;
		AuditRecord auditRecord;

		auditRecordId = super.getRequest().getData("id", int.class);
		codeAudit = this.repository.findOneCodeAuditByAuditRecordId(auditRecordId);
		auditRecord = this.repository.findOneAuditRecordById(auditRecordId);
		status = auditRecord != null && auditRecord.getIsDraftMode() && codeAudit != null && codeAudit.isDraftMode() && super.getRequest().getPrincipal().hasRole(codeAudit.getAuditor());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AuditRecord object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneAuditRecordById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final AuditRecord object) {
		assert object != null;

		super.bind(object, "code", "periodInit", "periodEnd", "mark", "link", "period");
	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			AuditRecord existing;

			existing = this.repository.findOneAuditRecordByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "auditor.audit-record.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("periodInit"))
			if (!MomentHelper.isBefore(object.getPeriodInit(), object.getPeriodEnd()))
				super.state(false, "periodInit", "auditor.audit-record.form.error.init-after-end");

		if (!super.getBuffer().getErrors().hasErrors("periodEnd")) {
			Date minimumPeriod;
			minimumPeriod = MomentHelper.deltaFromMoment(object.getPeriodInit(), 1, ChronoUnit.HOURS);

			if (!MomentHelper.isBeforeOrEqual(minimumPeriod, object.getPeriodEnd()))
				super.state(false, "periodEnd", "auditor.audit-record.form.error.too-close");
		}
	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;

		long diffInMilliseconds = Math.abs(object.getPeriodEnd().getTime() - object.getPeriodInit().getTime());

		double diffInDays = diffInMilliseconds / (1000.0 * 60 * 60 * 24);

		object.setPeriod(diffInDays);

		object.setIsDraftMode(false);

		this.repository.save(object);

		CodeAudit codeAudit = object.getCodeAudit();

		List<Mark> marks = this.repository.findManyMarksByCodeAuditId(codeAudit.getId());
		Mark modeMark = this.repository.getMode(marks);

		codeAudit.setMark(modeMark);

		this.repository.save(codeAudit);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		Dataset dataset;
		SelectChoices choicesMark;

		choicesMark = SelectChoices.from(Mark.class, object.getMark());

		dataset = super.unbind(object, "code", "periodInit", "periodEnd", "mark", "link", "period", "isDraftMode");
		dataset.put("masterId", object.getCodeAudit().getId());
		dataset.put("draftMode", object.getCodeAudit().isDraftMode());
		dataset.put("marks", choicesMark);

		super.getResponse().addData(dataset);
	}

}
