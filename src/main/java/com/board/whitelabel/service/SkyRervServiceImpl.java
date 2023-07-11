package com.board.whitelabel.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.board.whitelabel.dto.KakaoPayApprovalDTO;
import com.board.whitelabel.dto.ModifyDTO;
import com.board.whitelabel.dto.SkyAirTicketDTO;
import com.board.whitelabel.entity.SkyRerv;
import com.board.whitelabel.repository.SkyRervRepository;

@Service
public class SkyRervServiceImpl implements SkyRervService{

	@Autowired
	private SkyRervRepository skyRervRepository;
	
	@Override
	public void getRerv(SkyAirTicketDTO skaDto, KakaoPayApprovalDTO kpaDto) {
		// TODO Auto-generated method stub
		SkyRerv rerv = dtoToEntity(skaDto, kpaDto);
		skyRervRepository.save(rerv);
	}
	
	@Override
	public List<SkyRerv> getPassenger(String userName, String passport, Model model) {
		
		List<SkyRerv> rerv = skyRervRepository.findByUserNameAndPassport(userName, passport);
		
		return rerv;
		
	}

	@Override
	public SkyRerv getReservation(Long ono) {
		Optional<SkyRerv> optionalReservation = skyRervRepository.findById(ono);
		
	    if (optionalReservation.isPresent()) {
	        return optionalReservation.get();
	    } else {
	        // 예외 처리 (예약 정보를 찾을 수 없는 경우 등)
	        return null;
	    }
	}

	@Override
	public void remove(Long ono) {
		// TODO Auto-generated method stub
		skyRervRepository.deleteById(ono);
		
	}

	@Override
	public void modify(ModifyDTO dto) {
		// TODO Auto-generated method stub
		SkyRerv rerv = skyRervRepository.getOne(dto.getOno());
		
		rerv.changeTitle(dto.getUserName());
		rerv.changePassport(dto.getPassport());
		rerv.changeGender(dto.getGender());
		System.out.println("rerv 수정내용"+rerv);
		skyRervRepository.save(rerv);
		
	}
	
	
}
