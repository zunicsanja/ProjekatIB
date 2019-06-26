package ib.project.controller;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ib.project.certificate.CertificateGenerator;
import ib.project.common.DeviceProvider;
import ib.project.keystore.IssuerData;
import ib.project.keystore.KeyStoreWriter;
import ib.project.keystore.SubjectData;
import ib.project.model.User;
import ib.project.model.UserDTO;
import ib.project.model.UserTokenState;
import ib.project.security.TokenHelper;
import ib.project.security.auth.JwtAuthenticationRequest;
import ib.project.service.impl.CustomUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyPair;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping( value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE )
public class AuthenticationController {
	@Autowired
    TokenHelper tokenHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Autowired
    private DeviceProvider deviceProvider;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    //Ukoliko je aplikacija podignuta lokalno => localhost:8080/api/login
    //Metodi se prosledjuje objekat u kom se nalazi username i password korisnika
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest,
            HttpServletResponse response,
            Device device
    ) throws AuthenticationException, IOException {

        // Izvrsavanje security dela
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        // Ubaci username + password u kontext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token
        User user = (User)authentication.getPrincipal();
        String jws = tokenHelper.generateToken( user.getUsername(), device);
        int expiresIn = tokenHelper.getExpiredIn(device);
        
        // Vrati token kao odgovor na uspesno autentifikaciju
        return ResponseEntity.ok(new UserTokenState(jws, expiresIn));
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public ResponseEntity<?> refreshAuthenticationToken(
            HttpServletRequest request,
            HttpServletResponse response,
            Principal principal
            ) {

        String authToken = tokenHelper.getToken( request );

        Device device = deviceProvider.getCurrentDevice(request);

        if (authToken != null && principal != null) {

            // TODO check user password last update
            String refreshedToken = tokenHelper.refreshToken(authToken, device);
            int expiresIn = tokenHelper.getExpiredIn(device);

            return ResponseEntity.ok(new UserTokenState(refreshedToken, expiresIn));
        } else {
            UserTokenState userTokenState = new UserTokenState();
            return ResponseEntity.accepted().body(userTokenState);
        }
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChanger passwordChanger) {
        userDetailsService.changePassword(passwordChanger.oldPassword, passwordChanger.newPassword);
        Map<String, String> result = new HashMap<>();
        result.put( "result", "success" );
        return ResponseEntity.accepted().body(result);
    }

    static class PasswordChanger {
        public String oldPassword;
        public String newPassword;
    }
    
    @RequestMapping(value="/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) throws ParseException{
    	User user = new User();
    	
    	CertificateGenerator gen = new CertificateGenerator();
    	KeyPair keyPair = gen.generateKeyPair();
    	
    	SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
    	Date startDate = iso8601Formater.parse("2019-12-31");
    	Date endDate = iso8601Formater.parse("2029-12-31");
    	
    	X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
    	builder.addRDN(BCStyle.CN, userDTO.getFirstname()+" "+userDTO.getLastname());
    	builder.addRDN(BCStyle.SURNAME, userDTO.getLastname());
    	builder.addRDN(BCStyle.GIVENNAME, userDTO.getFirstname());
    	builder.addRDN(BCStyle.O, "UNS-FTN");
    	builder.addRDN(BCStyle.OU, "Katedra za informatiku");
    	builder.addRDN(BCStyle.C, "RS");
    	builder.addRDN(BCStyle.E, userDTO.getEmail());
    	builder.addRDN(BCStyle.UID, "12345");
    	
    	
    	String sn = "1";
    	IssuerData issuerData = new IssuerData(keyPair.getPrivate(), builder.build());
    	SubjectData subjectData = new SubjectData(keyPair.getPublic(), builder.build(), sn, startDate, endDate);
    	
    	X509Certificate cert = gen.generateCertificate(issuerData, subjectData);
    	
    	KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
    	keyStoreWriter.loadKeyStore(null, userDTO.getUsername().toCharArray());
    	keyStoreWriter.write(userDTO.getUsername(), keyPair.getPrivate(), "test10".toCharArray(), cert);
    	keyStoreWriter.saveKeyStore("D:\\IBPROJEKAT\\ProjekatIB\\IB_Project_Shel\\data\\" + userDTO.getUsername()+".jks", "test10".toCharArray());
    	
    	
    	String username = userDTO.getUsername();
    	user.setUsername(username);
    	String password = userDTO.getPassword();
    	user.setPassword(passwordEncoder.encode(password));
    	user.setEnabled(false);
    	user.setEmail(userDTO.getEmail());
    	user.setCertificate("D:\\IBPROJEKAT\\ProjekatIB\\IB_Project_Shel\\data\\"+ userDTO.getUsername()+".jks");
    	userDetailsService.saveUser(user);
    	
    	Map<String, String> result = new HashMap<>();
    	result.put("result", "success");
    	return ResponseEntity.accepted().body(result);
    	}
    	
}
    


