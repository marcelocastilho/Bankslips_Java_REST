package com.bankslips.entities.datatransformation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import com.bankslips.entities.BankSlip;
import com.bankslips.jpa.entities.BankSlipJPAEntity;

public class EntitiesTransformation {
	static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public static BankSlip convertJPAEntityToPOJOEntity(BankSlipJPAEntity jpaEntity) {

		ModelMapper modelMapper = new ModelMapper();
		Converter<LocalDate, String> toSimpleDateFormat = ctx -> ctx.getSource() == null ? null : dateFormatter.format(ctx.getSource());		
		Converter<String, Optional<String>> toString = ctx -> ctx.getSource() == null ? null :  Optional.of(ctx.getSource());		
		modelMapper.typeMap(BankSlipJPAEntity.class, BankSlip.class)
        .addMappings(mapper -> mapper.using(toSimpleDateFormat).map(BankSlipJPAEntity::getDueDate, BankSlip::setDueDate))
        .addMappings(mapper -> mapper.using(toString).map(BankSlipJPAEntity::getId, BankSlip::setId));
			
		BankSlip bankSlipEntity = modelMapper.map(jpaEntity, BankSlip.class);
		
		return bankSlipEntity;
	}
	
	public static BankSlipJPAEntity convertPOJOEntityToJPAEntity(BankSlip entity) {

		ModelMapper modelMapper = new ModelMapper();
		Converter<String, LocalDate> toSimpleDateFormat = ctx -> ctx.getSource() == null ? null : LocalDate.parse(ctx.getSource());		
		Converter<Optional<String>, String> toString = ctx -> !ctx.getSource().isPresent() ? null :  ctx.getSource().get();		
		
		modelMapper.typeMap(BankSlip.class, BankSlipJPAEntity.class)
        .addMappings(mapper -> mapper.using(toSimpleDateFormat).map(BankSlip::getDueDate, BankSlipJPAEntity::setDueDate))
        .addMappings(mapper -> mapper.using(toString).map(BankSlip::getId, BankSlipJPAEntity::setId));
			
		BankSlipJPAEntity bankSlipJPAEntity = modelMapper.map(entity, BankSlipJPAEntity.class);
		
		return bankSlipJPAEntity;
	}
}
