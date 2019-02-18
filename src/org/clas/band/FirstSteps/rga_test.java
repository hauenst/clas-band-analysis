package org.clas.band.FirstSteps;
import java.io.Reader;

import org.jlab.groot.data.H1F;
import org.jlab.groot.data.H2F;
import org.jlab.groot.ui.TCanvas;

import org.jlab.jnp.hipo.data.HipoEvent;
import org.jlab.jnp.hipo.data.HipoGroup;
import org.jlab.jnp.hipo.data.HipoNode;
import org.jlab.jnp.hipo.io.HipoReader;

import org.jlab.clas.physics.*;

import org.jlab.io.evio.*;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Box.Filler;

public class rga_test {

	// =========================================================================================================
	public static void main(String[] args){		

		// ----------------------------------------------------------------------------------
		// Useful variables
		double Ebeam = 6.4;//GeV
		double mp = 0.93827; //GeV
		double mtar = mp;
		double rad2deg = 180./3.14159;

		// ----------------------------------------------------------------------------------
		// Event selection cuts
		double cut_ep = 2; //GeV
		double cut_chi2pid = 5;
		double cut_min_vz = -15;
		double cut_max_vz = 10;
		double cut_W = 0;
		
		// ----------------------------------------------------------------------------------
		// Declaring histograms
		// 1D histograms
		H1F h1_e_vz  = new H1F("h1_e_vz" ,"h1_e_vz" ,100, -50,50);		PrettyH1F(h1_e_vz ,"electron v_{z} [cm]"  ,"Counts",4);
		
		H1F h1_e_px  = new H1F("h1_e_px" ,"h1_e_px" ,100,-2, 2);		PrettyH1F(h1_e_px ,"electron p_{x} [GeV]" ,"Counts",4);
		H1F h1_e_py  = new H1F("h1_e_py" ,"h1_e_py" ,100,-2, 2);		PrettyH1F(h1_e_py ,"electron p_{y} [GeV]" ,"Counts",4);
		H1F h1_e_pz  = new H1F("h1_e_pz" ,"h1_e_pz" ,100, 0, 9);		PrettyH1F(h1_e_pz ,"electron p_{z} [GeV]" ,"Counts",4);
		H1F h1_e_p   = new H1F("h1_e_p"  ,"h1_e_p"  ,100, 0, 9);		PrettyH1F(h1_e_p  ,"electron |p| [GeV]"   ,"Counts",4);
		
		H1F h1_e_th  = new H1F("h1_e_th" ,"h1_e_th" ,100, 0,30);		PrettyH1F(h1_e_th ,"#theta_e [deg]","Counts",4);
		H1F h1_e_phi = new H1F("h1_e_phi","h1_e_phi",100,-190,190);		PrettyH1F(h1_e_phi,"#phi_e [deg]"  ,"Counts",4);
		
		H1F h1_p_px  = new H1F("h1_p_px" ,"h1_p_px" ,100,-2, 2);		PrettyH1F(h1_p_px ,"proton p_{x} [GeV]" ,"Counts",4);
		H1F h1_p_py  = new H1F("h1_p_py" ,"h1_p_py" ,100,-2, 2);		PrettyH1F(h1_p_py ,"proton p_{y} [GeV]" ,"Counts",4);
		H1F h1_p_pz  = new H1F("h1_p_pz" ,"h1_p_pz" ,100, 0, 9);		PrettyH1F(h1_p_pz ,"proton p_{z} [GeV]" ,"Counts",4);
		H1F h1_p_p   = new H1F("h1_p_p"  ,"h1_p_p"  ,100, 0, 9);		PrettyH1F(h1_p_p  ,"proton |p| [GeV]"   ,"Counts",4);
		
		H1F h1_p_th  = new H1F("h1_p_th" ,"h1_p_th" ,100, 0,80);		PrettyH1F(h1_p_th ,"#theta_p [deg]","Counts",4);
		H1F h1_p_phi = new H1F("h1_p_phi","h1_p_phi",100,-190,190);		PrettyH1F(h1_p_phi,"#phi_p [deg]"  ,"Counts",4);
		
		H1F h1_pmiss = new H1F("h1_pmiss","P_{miss}",100, 0, 5);		PrettyH1F(h1_pmiss,"Pm [GeV]"      ,"Counts",4);
		H1F h1_pmx   = new H1F("h1_pmx"  ,"h1_pmx"  ,100,-2, 2);		PrettyH1F(h1_pmx  ,"Pmx [GeV]"     ,"Counts",4);
		H1F h1_pmy   = new H1F("h1_pmy"  ,"h1_pmy"  ,100,-2, 2);		PrettyH1F(h1_pmy  ,"Pmy [GeV]"     ,"Counts",4);
		H1F h1_pmz   = new H1F("h1_pmz"  ,"h1_pmz"  ,100,-3, 3);		PrettyH1F(h1_pmz  ,"Pmz [GeV]"     ,"Counts",4);

		H1F h1_W     = new H1F("h1_W"    ,"h1_W"    ,100, 0, 4);		PrettyH1F(h1_W    ,"W [GeV]"       ,"Counts",4);
		H1F h1_xB    = new H1F("h1_xB"   ,"h1_xB"   ,100, 0, 4);		PrettyH1F(h1_xB   ,"x_B"           ,"Counts",4);
		
		// 2D histograms
		H2F h2_e_Ep_p   = new H2F("h2_e_Ep_p"    ,"h2_e_Ep_p"    ,100,1   ,  7,100,0,0.3);	
		H2F h2_e_th_phi = new H2F("h2_e_th_phi","h2_e_th_phi",100,-190,190,100,0, 30);
		H2F h2_p_th_phi = new H2F("h2_p_th_phi","h2_p_th_phi",100,-190,190,100,0, 80);
		H2F h2_beta_p_0 = new H2F("h2_beta_p_0","h2_beta_p_0",100,1   ,  4,100,0,1.1);
		H2F h2_beta_p_1 = new H2F("h2_beta_p_1","h2_beta_p_1",100,1   ,  4,100,0,1.1);
		
		PrettyH2F(h2_e_Ep_p  ,"p_{e}"       ,"E_{e}/p_{e}"   );
		PrettyH2F(h2_e_th_phi,"#phi_e [deg]","#theta_e [deg]");
		
		// ----------------------------------------------------------------------------------
		// Opening HIPO file
		HipoReader reader = new HipoReader();
		//String dataFile = "/Users/efrainsegarra/Documents/band/prod_data/clas_006194.evio.00037.hipo"; // target lH2
		String dataFile = "/Users/Reynier/WORK/CLAS12/data/cooked/rga/out_clas_003842.evio.295.hipo"; // target lH2
		reader.open(dataFile);
		GenericKinematicFitter fitter = new GenericKinematicFitter(Ebeam);

		// ----------------------------------------------------------------------------------
		// Loop over events and print them on the screen
		while(reader.hasNext()==true){
			HipoEvent event = reader.readNextEvent();
			//event.show();

			if(     (event.hasGroup("RECHB::Particle"))&&
					(event.hasGroup("RECHB::Calorimeter"))
					){	

				HipoGroup bank_particle    = event.getGroup("RECHB::Particle"   );
				HipoGroup bank_calorimeter = event.getGroup("RECHB::Calorimeter");

				//bank_particle.show();
				//bank_calorimeter.show();
				
				// -------------------------------------------------------------------------
				// Electron PID from Dan Carman
				// - (DONE)	pid=11 from EB
				// - (DONE)	p > 2 GeV
				// - (DONE)	p < Ebeam
				// - 		TOF > 10 ns
				// - (DONE)	vz: -15 to 10 cm
				// - (DONE)	W > 0 GeV
				// - 		Sampling fraction +/-3sigma about E/p vs. p mean
				// - 		~15 cm fiducial cuts on U, V, W to contain full shower
				// - (DONE)	abs(chisq PID) < 5 (goodness of PID from EB)
				// - 		PCAL > 60 MeV (to remove min-i)
				// -------------------------------------------------------------------------
				
				// first particle variables
				int pid0       = bank_particle.getNode("pid"    ).getInt  (0);		// id assigned by clas
				int chr0       = bank_particle.getNode("charge" ).getInt  (0);		// charge
				double chi2pid = bank_particle.getNode("chi2pid").getFloat(0);		// goodness of PID from EB
				double epx     = bank_particle.getNode("px"     ).getFloat(0);		// momentum x-component [GeV]
				double epy     = bank_particle.getNode("py"     ).getFloat(0);		// momentum y-component [GeV]
				double epz     = bank_particle.getNode("pz"     ).getFloat(0);		// momentum z-component [GeV]
				double evx     = bank_particle.getNode("vx"     ).getFloat(0);		// vertex x coordinate [cm]
				double evy     = bank_particle.getNode("vy"     ).getFloat(0);		// vertex y coordinate [cm]
				double evz     = bank_particle.getNode("vz"     ).getFloat(0);		// vertex z coordinate [cm]
				
				double Ee      = bank_calorimeter.getNode("energy").getFloat(0);	// energy from calorimeter [GeV]
				
				// calculated variables
				double ep     = Math.sqrt(epx*epx + epy*epy + epz*epz);		// electron momentum magnitude [GeV]
				Vector3 v3_ep = new Vector3(epx,epy,epz);					
				double th_e   = v3_ep.theta();//Math.acos(epz/ep);			// electron theta [rad]
				double phi_e  = v3_ep.phi();								// electron phi [rad]
				double Q2     = 4*ep*Ebeam*Math.pow(Math.sin(th_e/2.),2);	// Q-squared [GeV^2]
				double nu     = Ebeam - ep;									// Transfer energy [GeV]
				double W2     = mtar*mtar-Q2+2*nu*mtar;						// Invariant mass ^2 [GeV]
				double xB     = Q2/2./mp/nu;								// Bjorken-x
				
				// Transfer variables
				double qx = - epx;
				double qy = - epy;
				double qz = Ebeam - epz;

				// -------------------------------------------------------------------------
				// Only keep events for which the first particle is an electron
				if(     (pid0!=11            )||
						(chr0!=-1            )||
						(chi2pid>=cut_chi2pid)||
						(ep<=cut_ep          )||
						(ep>=Ebeam           )||
						(evz>cut_max_vz      )||
						(evz<cut_min_vz      )||
						(Math.sqrt(W2)<=cut_W)
						) continue;

				// Filling electron histograms
				h1_e_px .fill(epx          );
				h1_e_py .fill(epy          );
				h1_e_pz .fill(epz          );
				h1_e_p  .fill(ep           );
				h1_e_vz .fill(evz          );
				h1_W    .fill(Math.sqrt(W2));
				h1_xB   .fill(xB           );
				h1_e_th .fill(rad2deg*th_e );
				h1_e_phi.fill(rad2deg*phi_e);
				
				h2_e_Ep_p.fill(ep,Ee/ep);
				h2_e_th_phi.fill(rad2deg*phi_e,rad2deg*th_e);
				
				// -------------------------------------------------------------------------
				// Loop over the remaining particles and require a proton
				int nParticles = bank_particle.getNode("pid").getDataSize();
				for(int par = 1; par < nParticles; par++) {
					
					int pidi      = bank_particle.getNode("pid"   ).getInt  (par);
					int chri      = bank_particle.getNode("charge").getInt  (par);
					double beta_p = bank_particle.getNode("beta"  ).getFloat(par);
					double ppx    = bank_particle.getNode("px"    ).getFloat(par);
					double ppy    = bank_particle.getNode("py"    ).getFloat(par);
					double ppz    = bank_particle.getNode("pz"    ).getFloat(par);
					
					double pp     = Math.sqrt(ppx*ppx + ppy*ppy + ppz*ppz);
					Vector3 v3_pp = new Vector3(ppx,ppy,ppz);					
					double th_p   = v3_pp.theta();			// proton theta [rad]
					double phi_p  = v3_pp.phi();			// proton phi [rad]
					
					// Fill some histograms before checking the particles are protons
					h2_beta_p_0.fill(pp, beta_p);
					
					if(     (pidi==2212)&&
							(chri==1)
							) {

						double pmx = ppx - qx;
						double pmy = ppy - qy;
						double pmz = ppz - qz;
						double Pm = Math.sqrt(pmx*pmx + pmy*pmy + pmz*pmz);

						h2_beta_p_1.fill(pp, beta_p);
						
						h1_p_px .fill(ppx);
						h1_p_py .fill(ppy);
						h1_p_pz .fill(ppz);
						h1_p_p  .fill(pp );
						
						h1_p_th .fill(rad2deg*th_p);
						h1_p_phi.fill(rad2deg*phi_p);
						h2_p_th_phi.fill(rad2deg*phi_p, rad2deg*th_p);
						
						// Missing momentum histograms
						h1_pmx  .fill(pmx);
						h1_pmy  .fill(pmy);
						h1_pmz  .fill(pmz);
						h1_pmiss.fill(Pm);
					}
				}

			}

		}
		// -------------------------------------------------------------------------------------------
		// Drawing histograms
		TCanvas c0 = new TCanvas("c0", 800, 600);
		c0.draw(h1_e_vz);
		
		TCanvas c1 = new TCanvas("c1", 800, 600);
		c1.divide(2, 2);
		c1.cd(0);	c1.draw(h1_pmx);
		c1.cd(1);	c1.draw(h1_pmy);
		c1.cd(2);	c1.draw(h1_pmz);
		c1.cd(3);	c1.draw(h1_pmiss);

		TCanvas c2 = new TCanvas("c2", 800, 600);
		c2.divide(2, 2);
		c2.cd(0);	c2.draw(h1_e_th);
		c2.cd(1);	c2.draw(h2_e_th_phi);
		c2.cd(3);	c2.draw(h1_e_phi);
		
		TCanvas c3 = new TCanvas("c3", 800, 600);
		c3.draw(h1_W);

		TCanvas c4 = new TCanvas("c4", 800, 600);
		c4.draw(h1_xB);
		
		TCanvas c5 = new TCanvas("c5", 800, 600);
		c5.draw(h2_e_Ep_p);
		
		TCanvas c6 = new TCanvas("c6", 800, 600);
		c6.divide(2, 2);
		c6.cd(0);	c6.draw(h1_e_px);
		c6.cd(1);	c6.draw(h1_e_py);
		c6.cd(2);	c6.draw(h1_e_pz);
		c6.cd(3);	c6.draw(h1_e_p );
		
		TCanvas c7 = new TCanvas("c7", 800, 600);
		c7.divide(2, 2);
		c7.cd(0);	c7.draw(h1_p_px);
		c7.cd(1);	c7.draw(h1_p_py);
		c7.cd(2);	c7.draw(h1_p_pz);
		c7.cd(3);	c7.draw(h1_p_p );
		
		TCanvas c8 = new TCanvas("c8", 800, 600);
		c8.divide(2, 1);
		c8.cd(0);	c8.draw(h2_beta_p_0);
		c8.cd(1);	c8.draw(h2_beta_p_1);
		
		TCanvas c9 = new TCanvas("c9", 800, 600);
		c9.divide(2, 2);
		c9.cd(0);	c9.draw(h1_p_th);
		c9.cd(1);	c9.draw(h2_p_th_phi);
		c9.cd(3);	c9.draw(h1_p_phi);

	}
	// =========================================================================================================
	public static void PrettyH1F(H1F h1,String titx,String tity,int color) {
		h1.setTitleX(titx);
		h1.setTitleY(tity);
		h1.setLineColor(color);
		h1.setLineWidth(3);
	}
	// =========================================================================================================
		public static void PrettyH2F(H2F h2,String titx,String tity) {
			h2.setTitleX(titx);
			h2.setTitleY(tity);
		}
}
