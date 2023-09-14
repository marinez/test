package fr.insy2s.sesame.service;


public interface IMailService {

    /**
     * Send a mail to the user.
     *
     * @param to the email of the user.
     * @param subject the subject of the mail.
     * @param content the content of the mail.
     * @author Fethi Benseddik
     */
    void sendMail(String to, String subject, String content);

    /**
     * Send a mail to the user for activated accoount.
     *
     * @param to the email of the user.
     * @param username the username of the user.
     * @param activationKey the activation key of the user.
     * @author Fethi Benseddik
     */
    void sendActivationEmail(String to, String username, String activationKey);

    /**
     * Send a mail to the user for reset password.
     * @param to the email of the user.
     */
    void sendNotificationPasswordUpdated(String to);

}
