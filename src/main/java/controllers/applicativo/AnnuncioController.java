//package main.java.controllers.applicativo;
//
//import main.java.engclasses.beans.AnnuncioBean;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import main.java.engclasses.dao.AnnuncioDAO;
//import main.java.model.Annuncio;
//
//public class AnnuncioController {
//
//    //ricevere gli annunci dal database
//    public List<AnnuncioBean> getAnnunci() {
//
//        List<Annuncio> annunci = AnnuncioDAO.getAnnunci();      //recupero annunci da dbms (come entity)
//        List<AnnuncioBean> annunciBean = new ArrayList<>();     //creo una lista dove inserire i bean degli annunci che prendo da dbms
//
//        for (Annuncio annuncio : annunci){
//            AnnuncioBean bean = BeanFactory.createAnnuncioBean(annuncio);   //converto gli annunci da model a bean
//            annunciBean.add(bean);
//        }
//
//        return annunciBean;
//    }



//}
