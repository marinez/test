package fr.insy2s.sesame.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.insy2s.sesame.dto.request.AddressRegisterRequest;
import fr.insy2s.sesame.dto.request.OrganizationRegisterRequest;
import fr.insy2s.sesame.dto.request.RegisterRequest;
import fr.insy2s.sesame.service.IAuthenticationService;
import fr.insy2s.sesame.service.IAuthorityService;
import fr.insy2s.sesame.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerWithMockTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    IAuthenticationService authenticationService;
    @MockBean
    IAuthorityService authorityService;
    @MockBean
    IUserService userService;


    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequest registerRequest;

    private OrganizationRegisterRequest organization;

    private AddressRegisterRequest address;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        address = AddressRegisterRequest.builder()
                .street("street")
                .city("city")
                .zipCode("12345")
                .build();
        organization = OrganizationRegisterRequest.builder()
                .businessName("businessName")
                .rib("012345678901234567891234568")
                .siren("123456789")
                .activity("activity")
                .capital(1000L)
                .directorName("directorName")
                .address(address)
                .businessCreationDate(LocalDate.now().minusYears(1))
                .build();
        registerRequest = RegisterRequest.builder()
                .firstName("Azeé èàçüö-ûîôâê'ëï")
                .lastName("lastName")
                .birthday(LocalDate.now().minusYears(20))
                .email("email@email.com").phone("01 02 03 04 05")
                .authority("ROLE_USER")
                .organization(organization)
                .post("")
                .activityZone("")
                .activityService("")
                .typeContract(null)
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegister() throws Exception {
        when(userService.emailAlreadyExists(any())).thenReturn(false);
        when(authorityService.authorityExist(any())).thenReturn(true);
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "COMMERCIAL")
    void testRegisterNoAuthorized() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterFirstNameNotBlank() throws Exception {
        registerRequest.setFirstName("      ");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'firstName' && @.message=='ne doit pas être vide.')]").exists());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterFirstNameWrongSize() throws Exception {
        registerRequest.setFirstName("a");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'firstName' && @.message=='la taille doit être comprise entre 2 et 50')]").exists());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterFirstNameWrongRegex() throws Exception {
        registerRequest.setFirstName("a2");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'firstName' && @.message=='doit contenir uniquement des lettres, apostrophes, espaces et tirets')]").exists());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterLastNameNotBlank() throws Exception {
        registerRequest.setLastName("      ");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'lastName' && @.message=='ne doit pas être vide.')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterLastNameWrongSize() throws Exception {
        registerRequest.setLastName("a");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'lastName' && @.message=='la taille doit être comprise entre 2 et 50')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterLastNameWrongRegex() throws Exception {
        registerRequest.setLastName("a!");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'lastName' && @.message=='doit contenir uniquement des lettres, apostrophes, espaces et tirets')]").exists());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterEmailNotBlank() throws Exception {
        registerRequest.setEmail("   ");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'email' && @.message=='ne doit pas être vide.')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterEmailInvalid() throws Exception {
        registerRequest.setEmail("wrongemail");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'email' && @.message=='doit être une adresse email bien formée')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterPhoneNotBlank() throws Exception {
        registerRequest.setPhone("      ");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'phone' && @.message=='ne doit pas être vide.')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterPhoneWrongSize() throws Exception {
        registerRequest.setPhone("1");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'phone' && @.message=='la taille doit être comprise entre 10 et 20')]").exists());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterPhoneWrongRegex() throws Exception {
        registerRequest.setPhone("3122313122313");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'phone' && @.message=='doit être au format 01 23 45 67 89 ou +33 6 23 45 67 89')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterAuthorityNotBlank() throws Exception {
        registerRequest.setAuthority("             ");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'authority' && @.message=='ne doit pas être vide.')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterPostWrongSize() throws Exception {
        registerRequest.setPost("a");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'post' && @.message=='doit contenir entre 2 et 50 caractères uniquement des lettres, apostrophes, espaces et tirets')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterPostWrongRegex() throws Exception {
        registerRequest.setPost("aaaa!");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'post' && @.message=='doit contenir entre 2 et 50 caractères uniquement des lettres, apostrophes, espaces et tirets')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterActivityZoneWrongSize() throws Exception {
        registerRequest.setActivityZone("a");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'activityZone' && @.message=='doit contenir entre 2 et 50 uniquement des lettres, chiffres, apostrophes, espaces et tirets')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterActivityZoneWrongRegex() throws Exception {
        registerRequest.setActivityZone("aaaa!");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'activityZone' && @.message=='doit contenir entre 2 et 50 uniquement des lettres, chiffres, apostrophes, espaces et tirets')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterActivityServiceWrongSize() throws Exception {
        registerRequest.setActivityService("a");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'activityService' && @.message=='doit contenir entre 2 et 50 uniquement des lettres, chiffres, apostrophes, espaces et tirets')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterActivityServiceWrongRegex() throws Exception {
        registerRequest.setActivityService("aaaa!");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'activityService' && @.message=='doit contenir entre 2 et 50 uniquement des lettres, chiffres, apostrophes, espaces et tirets')]").exists());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testOrganizationNotNull() throws Exception {
        registerRequest.setOrganization(null);
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'organization' && @.message=='ne doit pas être null.')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterBusinessNameNotBlank() throws Exception {
        organization.setBusinessName("      ");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'organization.businessName' && @.message=='ne doit pas être vide.')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterSirenWrongSize() throws Exception {
        organization.setSiren("12345678");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'organization.siren' && @.message=='la taille doit être comprise entre 9 et 9')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterSirenWrongPattern() throws Exception {
        organization.setSiren("12345678!");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'organization.siren' && @.message=='doit contenir 9 chiffres')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterActivityNotBlank() throws Exception {
        organization.setActivity("      ");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'organization.activity' && @.message=='ne doit pas être vide.')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterBusinessCreationDateNotNull() throws Exception {
        organization.setBusinessCreationDate(null);
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'organization.businessCreationDate' && @.message=='ne doit pas être null.')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterBusinessCreationDateNotFutur() throws Exception {
        organization.setBusinessCreationDate(LocalDate.now().plusDays(1));
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'organization.businessCreationDate' && @.message=='doit être une date dans le passé')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterCapitalNotNull() throws Exception {
        organization.setCapital(null);
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'organization.capital' && @.message=='ne doit pas être null.')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterDirectorNameNotBlank() throws Exception {
        organization.setDirectorName("      ");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'organization.directorName' && @.message=='ne doit pas être vide.')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterRibNotBlank() throws Exception {
        organization.setRib("                                    ");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'organization.rib' && @.message=='ne doit pas être vide.')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterRibWrongSize() throws Exception {
        organization.setRib("000000000");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'organization.rib' && @.message=='la taille doit être comprise entre 20 et 34')]").exists());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterAddressNotNull() throws Exception {
        organization.setAddress(null);
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'organization.address' && @.message=='ne doit pas être null.')]").exists());
        ;
    }

   /** @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterAddressStreetNotBlank() throws Exception {
        address.setStreet("      ");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'organization.address.street' && @.message=='ne doit pas être vide.')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterAddressCityNotBlank() throws Exception {
        address.setCity("      ");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'organization.address.city' && @.message=='ne doit pas être vide.')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterZipCodeNotBlank() throws Exception {
        address.setZipCode("      ");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'organization.address.zipCode' && @.message=='ne doit pas être vide.')]").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testRegisterAddressZipCodeWrongPattern() throws Exception {
        address.setZipCode("12345!");
        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(registerRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$[?(@.fieldName == 'organization.address.zipCode' && @.message=='doit contenir 5 chiffres')]").exists());
    }**/

}
