/**
 * Klassen sköter bankens grafiska gränssnitt
 *
 * @Author Martin Isaksen, marisk-1
 */
package marisk1.GUI;

import marisk1.bankLogics.BankLogic;
import marisk1.utils.SaveAndLoad;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

public class GUI extends JFrame {
    private static final int FRAME_WIDTH = 1024;
    private static final int FRAME_HEIGHT = 850;
    private final JLabel INFO_HANDLER = new JLabel();
    private final JLabel CUSTOMER_INFO = new JLabel();
    private final JLabel ACCOUNT_INFO = new JLabel();
    private BankLogic bankLogic;
    private String customerPno;
    private Integer customerAccountNumber;
    private File tempFileBankregister;
    private File tempFileTransactions;
    private JList<Object> accountList;
    private JList<Object> informationLog;
    private JList<Object> customerList;
    private JList<Object> popUpLog;
    private JPanel startPagePanel;
    private JPanel employeePanel;
    private JPanel btnPanel;
    private JPanel changeNamePanel;
    private JPanel createNewAccount;
    private JPanel createNewCustomerPanel;
    private JPanel lHPanelMain;
    private JPanel lHPanelSub;
    private JPanel confirmDeleteAccountPanel;
    private JPanel confirmDeleteCustomerPanel;
    private JTextField amount;
    private JTextField name;
    private JTextField surName;
    private JTextField personNumber;
    private JTextField nameAdd;
    private JTextField surNameAdd;
    private JDialog popUpTransactionLog;
    private JDialog popUpSaveDialog;

    public GUI() {
        bankLogic = new BankLogic();
        startUpFrameSet();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("LTU fictive bank");
    }

    /**
     * Metod som skapar upp "grundytan" Här läggs det in det in de två arbetsytorna
     * som jag valt att arbeta med och menyn till programmet.
     * Huvudpanelen väljer jag att inte använda något layoutmall för att jag vill
     * kunna växla mellan ytorna.
     */
    private void startUpFrameSet() {

        ActionListener listener = new MenuListener();

        startPagePanel = new JPanel(new BorderLayout());
        startPagePanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

        employeePanel = new JPanel(new BorderLayout());
        employeePanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createMenu(listener));

        JPanel framePanel = new JPanel();
        createStartPagePanel();
        framePanel.add(startPagePanel);
        createEmployeePanel();
        framePanel.add(employeePanel);
        employeePanel.setVisible(false);

        setJMenuBar(menuBar);
        add(framePanel);

        pack();
        popUpTransactionLog = createModalPopUp();
        popUpSaveDialog = createModalSavePopUp();

        customerList.setListData(bankLogic.getAllCustomers().toArray());
    }

    /**
     * Metod som skapar upp själva menyn. Här finns förbrett för spara och ladda.
     * Har lagt till iconer för de olika menyvalen
     *
     * @param listener event handler som lyssnar på vald meny
     * @return en komplett meny i JMenu format
     */
    private JMenu createMenu(ActionListener listener) {
        JMenu menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_M);

        JMenuItem menuItem = new JMenuItem("Load Bankregister",
                new ImageIcon("./marisk1_files/open-file-icon.png"));
        menuItem.setMnemonic(KeyEvent.VK_L);
        menuItem.addActionListener(listener);
        menu.add(menuItem);

        menuItem = new JMenuItem("Load Transactions",
                new ImageIcon("./marisk1_files/open-file-icon.png"));
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.addActionListener(listener);
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Save Bankregister",
                new ImageIcon("./marisk1_files/Save-icon.png"));
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.addActionListener(listener);
        menu.add(menuItem);

        menuItem = new JMenuItem("Save Transactions",
                new ImageIcon("./marisk1_files/Save-icon.png"));
        menuItem.setMnemonic(KeyEvent.VK_T);
        menuItem.addActionListener(listener);
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Log out",
                new ImageIcon("./marisk1_files/exit-icon.png"));
        menuItem.setMnemonic(KeyEvent.VK_L);
        menuItem.addActionListener(listener);
        menu.add(menuItem);

        menuItem = new JMenuItem("Exit application",
                new ImageIcon("./marisk1_files/exit-icon.png"));
        menuItem.setMnemonic(KeyEvent.VK_X);
        menuItem.addActionListener(listener);
        menu.add(menuItem);

        return menu;
    }

    /**
     * Metod som skapar upp "startsidans" arbetsyta. Här finns inloggning som Employee.
     * Övrigt en bild som inlagd då det ingår i VG. Sedan justering av Labeltext.
     * Sätter denna "arbetsyta" som synlig då den skall visas forsta.ser. Väljer att använda
     * borderlayout för placering av sakerna.
     */
    private void createStartPagePanel() {
        ImageIcon icon = new ImageIcon("./marisk1_files/bankIcon.png");
        JPanel mainContainer = new JPanel(new BorderLayout());
        ActionListener listener = new StartPageListener();

        JLabel label = new JLabel();
        Border border = BorderFactory.createLineBorder(Color.black, 1);
        label.setText("Welcome to the");
        label.setIcon(icon);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.TOP);
        label.setForeground(new Color(200, 135, 135));
        label.setFont(new Font("New Times Roman", Font.PLAIN, 40));
        label.setBorder(border);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);

        mainContainer.add(label, BorderLayout.NORTH);

        JLabel labelBtnPanel = new JLabel();
        labelBtnPanel.setText("Login as");
        labelBtnPanel.setFont(new Font("New Times Roman", Font.PLAIN, 40));
        labelBtnPanel.setVerticalAlignment(JLabel.CENTER);
        labelBtnPanel.setHorizontalAlignment(JLabel.CENTER);
        mainContainer.add(labelBtnPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.ipady = 40;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new Insets(0, 10, 30, 10);  //top padding
        JButton employeeButton = new JButton("Employee");
        employeeButton.setFont(new Font("New Times Roman", Font.PLAIN, 30));
        employeeButton.addActionListener(listener);

        btnPanel.add(employeeButton, gridBagConstraints);
        mainContainer.add(btnPanel, BorderLayout.SOUTH);
        startPagePanel.add(mainContainer);
        startPagePanel.setVisible(true);

    }

    /**
     * Metoden skapar upp huvudarbetsytan av själva banken. Arbetsytan delas upp
     * i maincontainer i 3 st kolumner med hjälp av Gridlayout. I dessa lägga sedan underpaneler för olika
     * användningsområden. Här används olika lyssnar för att dela upp områdena
     */
    private void createEmployeePanel() {
        ActionListener listener = new EmployeeMainBarListener();
        ActionListener subBarListener = new EmployeeSubBarListener();

        JPanel lHPanel = new JPanel(new GridLayout(3, 1));

        JPanel lHPanelTop = new JPanel();
        JPanel lHPanelCenter = new JPanel();
        JPanel lHPanelBottom = new JPanel();
        JPanel cMainContainer = new JPanel(new GridLayout(1, 3));

        lHPanelBottom.add(addLHBottomPanel(listener));
        lHPanelTop.add(addLhPanelMain(listener));
        lHPanelTop.add(addLhPanelSub(listener));
        lHPanelMain.setVisible(false);

        JPanel midPanel = new JPanel(new GridLayout(3, 1));
        JPanel midPanelTop = new JPanel(new GridLayout(8, 1));
        JPanel midPanelCenter = new JPanel();
        JPanel midPanelBottom = new JPanel(new GridLayout(1, 1));

        midPanelTop.setBorder(BorderFactory.createTitledBorder("Present"));
        midPanelTop.setBackground(Color.white);
        midPanelTop.add(labelSettings18Bold(new JLabel("Customer")));
        midPanelTop.add(labelSettings12Bold(CUSTOMER_INFO));
        midPanelTop.add(labelSettings18Bold(new JLabel("Account")));
        midPanelTop.add(labelSettings12Bold(ACCOUNT_INFO));

        midPanelCenter.add(createNewCustomerPanel(subBarListener));
        createNewCustomerPanel.setVisible(false);
        midPanelCenter.add(depositAndWithdrawPanel(subBarListener));
        btnPanel.setVisible(false);
        midPanelCenter.add(createNewAccountPanel(subBarListener));
        createNewAccount.setVisible(false);
        midPanelCenter.add(renameCustomerPanel(subBarListener));
        changeNamePanel.setVisible(false);
        midPanelCenter.add(createConfirmDeleteAccountPanel(subBarListener));
        confirmDeleteAccountPanel.setVisible(false);
        midPanelCenter.add(createConfirmDeleteCustomerPanel(subBarListener));
        confirmDeleteCustomerPanel.setVisible(false);

        midPanelBottom.add(informationLogPanel());

        JPanel rHpanel = new JPanel(new GridLayout(2, 1));
        rHpanel.add(customerListPanel());
        rHpanel.add(accountListPanel());

        lHPanel.add(lHPanelTop);
        lHPanel.add(lHPanelCenter);
        lHPanel.add(lHPanelBottom);

        midPanel.add(midPanelTop);
        midPanel.add(midPanelCenter);
        midPanel.add(midPanelBottom);

        cMainContainer.add(lHPanel);
        cMainContainer.add(midPanel);
        cMainContainer.add(rHpanel);

        employeePanel.add(cMainContainer);
        employeePanel.add(INFO_HANDLER, BorderLayout.SOUTH);
        employeePanel.setVisible(true);
    }

    /**
     * Skapar upp knappsatspanel för val av kundärende eller bankregister. Använder mig utav Gridbaglayout
     * för placering utav komponenterna.
     *
     * @param listener Actionlistener för knapparna
     * @return Knapppanel i JPanel
     */
    private JPanel addLHBottomPanel(ActionListener listener) {

        JPanel lHPanelBottom = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = buttonsGridBagConfig();

        JButton handleBank = new JButton("Handle customer in bankregister");
        handleBank.addActionListener(listener);
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 60;
        lHPanelBottom.add(handleBank, gridBagConstraints);

        JButton handleAccounts = new JButton("Handle accounts and transactions");
        handleAccounts.addActionListener(listener);
        gridBagConstraints.gridy = 0;
        lHPanelBottom.add(handleAccounts, gridBagConstraints);

        JButton clearScreen = new JButton("Clear customer");
        clearScreen.addActionListener(listener);
        gridBagConstraints.gridy = 2;
        lHPanelBottom.add(clearScreen, gridBagConstraints);

        return lHPanelBottom;
    }

    /**
     * Metod som skapar upp knappanelen för bankregisterhantering. Använder mig utav Gridbaglayout för
     * placering utav komponenterna
     *
     * @param listener Actionlistener för knapparna
     * @return Knappanel i JPanel
     */
    private JPanel addLhPanelMain(ActionListener listener) {
        lHPanelMain = new JPanel(new GridBagLayout());
        GridBagConstraints GridBagConfig = buttonsGridBagConfig();

        JButton addButton = new JButton("Add new customer");
        addButton.addActionListener(listener);
        GridBagConfig.gridy = 0;
        lHPanelMain.add(addButton, GridBagConfig);

        JButton deleteButton = new JButton("Delete customer");
        deleteButton.addActionListener(listener);
        GridBagConfig.gridy = 1;

        lHPanelMain.add(deleteButton, GridBagConfig);

        JButton replaceName = new JButton("Change Customer name");
        replaceName.addActionListener(listener);
        GridBagConfig.gridy = 2;
        lHPanelMain.add(replaceName, GridBagConfig);

        return lHPanelMain;
    }

    /**
     * Metod som skapar upp knappanel för kundärende hantering. Använder mig utav GridbagLayout
     * för placering utav komponenterna.
     *
     * @param listener Actionlistener för knapparna
     * @return Knappanel i JPanel
     */
    private JPanel addLhPanelSub(ActionListener listener) {
        lHPanelSub = new JPanel(new GridBagLayout());
        GridBagConstraints GridBagConfig = buttonsGridBagConfig();

        JButton createAccount = new JButton("Create new account");
        createAccount.addActionListener(listener);
        GridBagConfig.gridy = 0;
        lHPanelSub.add(createAccount, GridBagConfig);

        JButton makeTransactionButton = new JButton("Make a transaction");
        makeTransactionButton.addActionListener(listener);
        GridBagConfig.gridy = 1;
        lHPanelSub.add(makeTransactionButton, GridBagConfig);

        JButton seeTransactionButton = new JButton("See transactionhistory");
        seeTransactionButton.addActionListener(listener);
        GridBagConfig.gridy = 2;
        lHPanelSub.add(seeTransactionButton, GridBagConfig);

        JButton deleteAccountButton = new JButton("Delete account");
        deleteAccountButton.addActionListener(listener);
        GridBagConfig.gridy = 3;
        lHPanelSub.add(deleteAccountButton, GridBagConfig);

        return lHPanelSub;
    }

    /**
     * Skapar upp knappar för val utan ny skapat konto för Kredit eller spar konto. Använder mig utav GridbagLayout
     * för placering utav komponenter.
     *
     * @param listener Actionlistener för knapparna
     * @return Knappar i JPanel
     */
    private JPanel createNewAccountPanel(ActionListener listener) {
        createNewAccount = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = buttonsGridBagConfig();

        JButton createSavBtn = new JButton("Create Savingsaccount");
        createSavBtn.addActionListener(listener);
        gridBagConstraints.gridy = 0;
        createNewAccount.add(createSavBtn, gridBagConstraints);

        JButton employeeButton = new JButton("Create Creditaccount");
        employeeButton.addActionListener(listener);
        gridBagConstraints.gridy = 1;
        createNewAccount.add(employeeButton, gridBagConstraints);

        return createNewAccount;
    }

    /**
     * Metod som skapar upp panel för byte utav namn. Använder mig utav Gridbaglayout för placeringar
     * utav komponenter.
     *
     * @param listener Actionlistener för knapparna
     * @return Panel med knappar och inmatningsfält JPanel
     */
    private JPanel renameCustomerPanel(ActionListener listener) {

        changeNamePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = buttonsGridBagConfig();

        JButton changeNameBtn = new JButton("Change name");
        changeNameBtn.addActionListener(listener);
        gridBagConstraints.insets = new Insets(20, 0, 5, 0);
        gridBagConstraints.gridy = 3;
        changeNamePanel.add(changeNameBtn, gridBagConstraints);

        JButton clearBtn = new JButton("Clear Fields");
        clearBtn.addActionListener(listener);
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new Insets(0, 0, 5, 0);
        changeNamePanel.add(clearBtn, gridBagConstraints);

        name = new JTextField(10);
        name.setFont(new Font("New Times Roman", Font.PLAIN, 15));
        name.setBorder(BorderFactory.createTitledBorder("Name"));
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 10;
        changeNamePanel.add(name, gridBagConstraints);
        name.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                clearBottomLabel();
                char c = e.getKeyChar();
                if (!Character.isAlphabetic(c)) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        surName = new JTextField(10);
        surName.setFont(new Font("New Times Roman", Font.PLAIN, 15));
        surName.setBorder(BorderFactory.createTitledBorder("Surname"));
        gridBagConstraints.gridy = 2;
        changeNamePanel.add(surName, gridBagConstraints);
        surName.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                clearBottomLabel();
                char c = e.getKeyChar();
                if (!Character.isAlphabetic(c)) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        return changeNamePanel;
    }

    /**
     * Skapar upp arbetsyta för inmatning utav ny kund. Använder mig utav GridbagLayout för placering utav
     * komponenter.
     *
     * @param listener Actionlistener för knapparna
     * @return Panel med knappar och inmatningsfält JPanel
     */
    private JPanel createNewCustomerPanel(ActionListener listener) {

        createNewCustomerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = buttonsGridBagConfig();

        JButton addCustomer = new JButton("Create customer");
        addCustomer.addActionListener(listener);
        gridBagConstraints.gridy = 3;
        createNewCustomerPanel.add(addCustomer, gridBagConstraints);

        JButton clearBtn = new JButton("Clear Fields");
        clearBtn.addActionListener(listener);
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipady = 0;
        createNewCustomerPanel.add(clearBtn, gridBagConstraints);

        personNumber = new JTextField(10);
        personNumber.setFont(new Font("New Times Roman", Font.PLAIN, 15));
        personNumber.setBorder(BorderFactory.createTitledBorder("Person number"));
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 10;
        createNewCustomerPanel.add(personNumber, gridBagConstraints);
        personNumber.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                clearBottomLabel();
                char c = e.getKeyChar();
                if ((!Character.isDigit(c)) && (c != 45)) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        nameAdd = new JTextField(10);
        nameAdd.setFont(new Font("New Times Roman", Font.PLAIN, 15));
        nameAdd.setBorder(BorderFactory.createTitledBorder("Name"));
        gridBagConstraints.gridy = 1;
        createNewCustomerPanel.add(nameAdd, gridBagConstraints);
        nameAdd.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                clearBottomLabel();
                char c = e.getKeyChar();
                if (!Character.isAlphabetic(c)) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        surNameAdd = new JTextField(10);
        surNameAdd.setFont(new Font("New Times Roman", Font.PLAIN, 15));
        surNameAdd.setBorder(BorderFactory.createTitledBorder("Surname"));
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new Insets(0, 0, 20, 0);
        createNewCustomerPanel.add(surNameAdd, gridBagConstraints);
        surNameAdd.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                clearBottomLabel();
                char c = e.getKeyChar();
                if (!Character.isAlphabetic(c)) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        return createNewCustomerPanel;
    }

    /**
     * Skapar upp arbetsyta för kundärende gällande insättning och uttag. Använder mig utav Gridbaglayout för
     * placering utav komponenter
     *
     * @param listener Actionlistener för knapparna
     * @return Panel med knappar och inmatningsfält i JPanel
     */
    private JPanel depositAndWithdrawPanel(ActionListener listener) {

        btnPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = buttonsGridBagConfig();

        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(listener);
        gridBagConstraints.gridy = 1;
        btnPanel.add(depositButton, gridBagConstraints);

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(listener);
        gridBagConstraints.gridy = 2;
        btnPanel.add(withdrawButton, gridBagConstraints);

        amount = new JTextField(10);
        amount.setFont(new Font("New Times Roman", Font.PLAIN, 15));
        amount.setBorder(BorderFactory.createTitledBorder("Amount"));
        gridBagConstraints.insets = new Insets(0, 0, 25, 0);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 15;
        btnPanel.add(amount, gridBagConstraints);
        amount.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                clearBottomLabel();
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        return btnPanel;
    }

    /**
     * Skapar upp en knapp för konfirmering utav borttagning av konto. Använder mig utav Gridbaglayout för
     * placering utav kompnenter.
     *
     * @param listener Actionlistener för knapp
     * @return Panel med knapp i JPanel
     */
    private JPanel createConfirmDeleteAccountPanel(ActionListener listener) {

        confirmDeleteAccountPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = buttonsGridBagConfig();

        JButton confirmDelete = new JButton("Confirm delete account");
        confirmDelete.addActionListener(listener);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 60;
        confirmDeleteAccountPanel.add(confirmDelete, gridBagConstraints);

        return confirmDeleteAccountPanel;
    }

    /**
     * Skapar upp en knapp för konfirmering utav borttagning utav kund. Använder mig utav Gridbaglayout för
     * placering utav kompnenter.
     *
     * @param listener Actionlistener för knapp
     * @return Panel med knapp i JPanel
     */
    private JPanel createConfirmDeleteCustomerPanel(ActionListener listener) {

        confirmDeleteCustomerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = buttonsGridBagConfig();

        JButton confirmDeleteCustomer = new JButton("Confirm delete customer");
        confirmDeleteCustomer.addActionListener(listener);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 60;
        confirmDeleteCustomerPanel.add(confirmDeleteCustomer, gridBagConstraints);

        return confirmDeleteCustomerPanel;
    }

    /**
     * Inställningsmetod för JLabel
     *
     * @param input JLabel till korrigering
     * @return korrigerad JLabel
     */
    private JLabel labelSettings12Bold(JLabel input) {
        input.setHorizontalTextPosition(JLabel.CENTER);
        input.setVerticalTextPosition(JLabel.TOP);
        input.setOpaque(true);
        input.setFont(new Font("New Times Roman", Font.BOLD, 12));
        input.setVerticalAlignment(JLabel.CENTER);
        input.setHorizontalAlignment(JLabel.CENTER);
        input.setBackground(Color.white);
        return input;
    }

    /**
     * Inställningsmetod för JLabel
     *
     * @param input JLabel till korrigering
     * @return korrigerad JLabel
     */
    private JLabel labelSettings18Bold(JLabel input) {
        input.setHorizontalTextPosition(JLabel.CENTER);
        input.setVerticalTextPosition(JLabel.TOP);
        input.setOpaque(true);
        input.setFont(new Font("New Times Roman", Font.BOLD, 18));
        input.setVerticalAlignment(JLabel.CENTER);
        input.setHorizontalAlignment(JLabel.CENTER);
        input.setBackground(Color.white);
        return input;
    }

    /**
     * Metod som skapar upp informations fönster
     *
     * @return JList panel
     */
    private JList<Object> informationLogPanel() {
        informationLog = new JList();
        informationLog.setBorder(BorderFactory.createTitledBorder("Information"));
        return informationLog;
    }

    /**
     * Metod som skapar upp kundkonto fönster. Lyssnare på val utav värde i listan.
     *
     * @return JList panel
     */
    private JList<Object> accountListPanel() {
        accountList = new JList();
        accountList.setBorder(BorderFactory.createTitledBorder("Customer Accounts"));
        accountList.addListSelectionListener(e -> {
            //Kontrollerar om personnr saknas hoppar då ur lambdafunktionen och returnerar en tom lista
            if (customerPno == null) {
                return;
            }
            //Hämtar valda kundens kontouppgifter
            getAccountInfo();
            //Skriver ut kundens konto i Kontorutan
            ACCOUNT_INFO.setText(bankLogic.getAccount(customerPno, customerAccountNumber));
            //Hämtar transaktioner gjorda på kontot
            //Sätter Boolean till false för att metoden används av andra metoder där det behövs felhantering
            getTransactions(false);
            //Kontrollerar om där finns några transaktioner på konto för utrendering i informationsfönstret
            if (isCustomerPnoValid() && customerAccountNumber > 0)
                informationLog.setListData(bankLogic.getTransactions(customerPno, customerAccountNumber).toArray());
        });
        return accountList;
    }

    /**
     * Metod som skapar upp fönster med alla bankens kunder. Lyssnar på val utav värden i listan
     *
     * @return JList panel
     */
    private JList<Object> customerListPanel() {

        customerList = new JList<>();
        customerList.setBorder(BorderFactory.createTitledBorder("Bank Customers"));
        //Lyssnare för markering utav kunder i kundlistan. Samt hämtar upp alla kunder från registret
        customerList.addListSelectionListener(e -> {
            clearMidPanel();
            clearBottomLabel();
            getCustomerPnoFromList();
            CUSTOMER_INFO.setText(bankLogic.getBasicCustomerInfo(customerPno));
            //Nollställer valt kontonr.
            customerAccountNumber = null;
            ACCOUNT_INFO.setText("");
        });
        return customerList;
    }

    /**
     * Skapar upp en ny JList fönster för visningsmodal av transaktioner
     *
     * @return Jlist
     */
    private JList<Object> popUpPanel() {
        popUpLog = new JList();
        return popUpLog;
    }

    /**
     * Skapar upp popupmodal med gammal transaktionslog.
     *
     * @return JDialog popup modal
     */
    private JDialog createModalPopUp() {
        JDialog popUp = new JDialog(this, "Transactionlog");
        popUp.setLayout(new BorderLayout());
        popUp.setSize(400, 600);
        popUp.setLocationRelativeTo(this);
        JButton okBtn = new JButton("OK");

        okBtn.addActionListener(e -> popUpTransactionLog.setVisible(false));
        popUp.add(popUpPanel(), BorderLayout.CENTER);
        popUp.add(okBtn, BorderLayout.SOUTH);
        return popUp;
    }

    /**
     * Metod som skapar upp popup när man skall spara över.
     * Actionevent känner av vilken fil det är som är aktiv och kan skilja på transaktioner och bankregister
     * Sparar ned sedan om användare väljer så
     *
     * @return JDialog komponent
     */
    private JDialog createModalSavePopUp() {
        JDialog popUpSave = new JDialog(this, "Saving");
        popUpSave.setLayout(new BorderLayout());
        popUpSave.setSize(280, 150);
        popUpSave.setLocationRelativeTo(this);

        JLabel label1 = new JLabel("Do you wish to overwrite current file?", JLabel.CENTER);
        JButton okBtn = new JButton("Overwrite");
        JButton cancelBtn = new JButton("Cancel");
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));

        okBtn.addActionListener(e -> {

            if (tempFileBankregister != null) {
                setInfoHandler(bankLogic.saveBankRegisterToFile(tempFileBankregister), "savedFile");
                popUpSaveDialog.setVisible(false);
                tempFileBankregister = null;
            } else {
                setInfoHandler(bankLogic.saveTransactionsToFile(tempFileTransactions, customerPno, customerAccountNumber), "savedFile");
                popUpSaveDialog.setVisible(false);
                tempFileTransactions = null;
            }
        });
        cancelBtn.addActionListener(e -> popUpSaveDialog.setVisible(false));

        bottomPanel.add(okBtn);
        bottomPanel.add(cancelBtn);
        popUpSave.add(label1, BorderLayout.CENTER);
        popUpSave.add(bottomPanel, BorderLayout.SOUTH);
        return popUpSave;
    }

    /**
     * Metod som plockar ut personnr från vald kund i listan. Sparar personnr i klassvariabel.
     * Hämtar sedan kundens konto med hjälp av personnr. Har kunden konto sätts de i konto fönstret.
     * Om där uppstår fel med konvertering kastar den exception direkt och ingen kund väljs.
     */
    private void getCustomerPnoFromList() {
        try {
            //Plockar bort övriga delar av strängen än personnr
            customerPno = customerList.getSelectedValue().toString().replaceAll("\\s.*", "");
            //Om kontrollerar om kunden har konto då returneras listan annars sätts en tom lista
            if (bankLogic.getCustomerAccounts(customerPno) != null) {
                accountList.setListData(bankLogic.getCustomerAccounts(customerPno).toArray());
            } else {
                accountList.setListData(new ArrayList<String>().toArray());
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Metod som plockar ut kontonr från valt konto i kontolistan och sätter i klassvariabel.
     */
    private void getAccountInfo() {
        try {
            //Plockar bort övrig information från strängen som inte är kontonr och konvertar strng till Integer
            customerAccountNumber = Integer.parseInt(accountList.getSelectedValue().toString().replaceAll("\\s.*", ""));
        } catch (Exception ignored) {
        }
    }

    /**
     * Metod som skapar upp kunden. Felhanterare finns med då det returneras Sant eller falskt om skapandet gått igenom
     * setInfoHandler har hand om felhantering
     */
    private void createCustomer() {
        //Skickar in värden i Banklogic som provar att skapa upp ny kund. Blir där fel returneras falskt
        //och felhantering tar över. Om kunden skapas visas detta också.
        if (bankLogic.createCustomer(nameAdd.getText(), surNameAdd.getText(), personNumber.getText())) {
            //Lägger till kunden i kundlistan
            customerList.setListData(bankLogic.getAllCustomers().toArray());
            setInfoHandler(true, "createCustomer");
            nameAdd.setText("");
            surNameAdd.setText("");
            personNumber.setText("");
        } else setInfoHandler(false, "createCustomer");
    }

    /**
     * Metod som gör en insättning med felhantering. setInfoHandler tar hand om felhanteringen
     */
    private void deposit() {
        //Provar att göra insättning, lyckas den sätts nya värden i Kontofönstret samt transaktionen
        //visas upp i informationsfönstret. Misslyckas kopplas felhanteringen in.
        if (bankLogic.deposit(customerPno, customerAccountNumber, Integer.parseInt(amount.getText()))) {
            amount.setText("");
            accountList.setListData(bankLogic.getCustomerAccounts(customerPno).toArray());
            getTransactions(false);
            setInfoHandler(true, "deposit");
        } else {
            setInfoHandler(false, "deposit");
        }
    }

    /**
     * Metod som gör uttag med felhantering. setInfoHandler tar han om felhanteringen.
     */
    private void withDraw() {
        //Provar göra uttag om det lyckas uppdateras kontolistan och informationsfönstret.
        //Misslyckas kopplas felhanteringen in.
        if (bankLogic.withdraw(customerPno, customerAccountNumber, Integer.parseInt(amount.getText()))) {
            amount.setText("");
            accountList.setListData(bankLogic.getCustomerAccounts(customerPno).toArray());
            getTransactions(false);
            setInfoHandler(true, "withdraw");
        } else {
            setInfoHandler(false, "withdraw");
        }

    }

    /**
     * Metod som hämtar utförda kontotransaktioner och lägger automatiskt upp dom i transaktionfönstret.
     * Inkommande Boolean är endast till för att ha felhantering vid manuellt tryck på se transaktioner
     *
     * @param button Inkommande sant eller falskt för felhantering
     */
    private void getTransactions(boolean button) {

        if (isCustomerPnoValid() && isCustomerAccountValid()) {
            //Om där finns genomförda transaktioner på kontot visas de upp i transaktionsfönstret
            if (bankLogic.getTransactions(customerPno, customerAccountNumber).size() > 0) {
                informationLog.setListData(bankLogic.getTransactions(customerPno, customerAccountNumber).toArray());
                //Används endast som felhantering när man trycker på knappen See transactions
            } else if (button) {
                setInfoHandler(false, "No transactions");
            }
        }
    }

    /**
     * Metod som hanterar byte av namn. Körs en kontroll att båda fälten inte är tomma vid knapptryckning.
     * Felhanterar som informerar om detta
     */
    private void changeCustomerName() {
        if (!(name.getText().equals("") && surName.getText().equals(""))) {
            setInfoHandler(bankLogic.changeCustomerName(name.getText(), surName.getText(), customerPno),
                    "changeCustomerName");
            customerList.setListData(bankLogic.getAllCustomers().toArray());
        } else {
            setInfoHandler(false, "emptyField");
        }
    }

    /**
     * Metod som plockar bort kunden från bankens register.
     * Kontrollerar om personnr finns i banken, skriver sedan ut informationen i informationsfönstret
     */
    private void deleteCustomer() {

        if (isCustomerPnoValid()) {
            //Skapar upp en temp arraylista för att när uppdateringen av Kund fönstret uppdater
            // rensas informationsfönstret. Nu utförs borttagningen infon sparas i templistan som återanvänds
            // sist i metoden.
            ArrayList<String> tempList = bankLogic.deleteCustomer(customerPno);
            //Renderar ut ny kundlista
            customerList.setListData(bankLogic.getAllCustomers().toArray());
            //renderar ut info i informationsfönstret
            informationLog.setListData(tempList.toArray());
            setInfoHandler(true, "delete customer");
        }
    }

    /**
     * Bortplockning utav konto, Kontrollerar först att personnr och kontonr är rätt
     * Sparar även ner listan i en templist här för att informationen skall visas rätt i informationsfönstret
     */
    private void deleteAccount() {
        if (isCustomerPnoValid() && isCustomerAccountValid()) {
            ArrayList<String> tempList = bankLogic.closeAccount(customerPno, customerAccountNumber);
            accountList.setListData(bankLogic.getCustomerAccounts(customerPno).toArray());
            informationLog.setListData(tempList.toArray());
            setInfoHandler(true, "delete account");
        }
    }

    /**
     * Skapar upp ett nytt sparkonto och returen av nytt kontonr renderas ut i informations fönstret
     */
    private void createNewSaveAccount() {
        //Skapar nytt konto och sparar variabel
        int responseSaveAccountNr = bankLogic.createSavingsAccount(customerPno);
        //Kontrollerar att accountnr skapats
        if (responseSaveAccountNr > 0) {
            accountList.setListData(bankLogic.getCustomerAccounts(customerPno).toArray());
            //Skapar en templista för att skapa en sträng som sedan skall renderas ut i informations fönstret
            ArrayList<String> tempList = new ArrayList<>();
            tempList.add("New savingsaccount created nr " + responseSaveAccountNr);
            informationLog.setListData(tempList.toArray());
        }
    }

    /**
     * Skapar upp ett nytt kreditkonto och returen av nytt kontonr renderas ut i informations fönstret
     */
    private void createNewCredAccount() {
        int responseCredAccountNr = bankLogic.createCreditAccount(customerPno);
        if (responseCredAccountNr > 0) {
            accountList.setListData(bankLogic.getCustomerAccounts(customerPno).toArray());
            ArrayList<String> tempList = new ArrayList<>();
            tempList.add("New creditaccount created nr " + responseCredAccountNr);
            informationLog.setListData(tempList.toArray());
        }
    }

    /**
     * Helper för att kontrollera att kund finns i Klassvariablen
     * setInfoHandler tar hand om felhanteringen
     *
     * @return Boolean Sant om kund aktiverad false om kund inteaktiverad
     */
    private boolean isCustomerPnoValid() {
        if (customerPno != null) {
            return true;
        }
        setInfoHandler(false, "customerPno");
        return false;
    }

    /**
     * Helper som kontrollerar att där finns ett aktivt kontonr i klass variablen
     *
     * @return Sant om det finns, false om inget konto är aktivt
     */
    private boolean isCustomerAccountValid() {
        if (customerAccountNumber != null) {
            return true;
        }
        setInfoHandler(false, "customerAccount");
        return false;
    }

    /**
     * Felhanterare som skickar ut information i botten av skärmen vid misslyckande och vid ok transaktioner.
     * Använder mig utav Lambda Switch för att få en lätt förståelig kod.
     *
     * @param response Sant om uppgiften utförts korrekt, false om något fel uppstått
     * @param type     Sträng som anger vilken av aktion som utförts
     */
    private void setInfoHandler(boolean response, String type) {

        INFO_HANDLER.setHorizontalTextPosition(JLabel.CENTER);
        INFO_HANDLER.setVerticalTextPosition(JLabel.TOP);
        INFO_HANDLER.setOpaque(true);
        INFO_HANDLER.setFont(new Font("New Times Roman", Font.PLAIN, 15));
        INFO_HANDLER.setVerticalAlignment(JLabel.CENTER);
        INFO_HANDLER.setHorizontalAlignment(JLabel.CENTER);
        String noError = "executed without any errors";
        if (response) {
            INFO_HANDLER.setBackground(Color.green);
            switch (type) {
                case "deposit" -> INFO_HANDLER.setText("Deposit " + noError);
                case "withdraw" -> INFO_HANDLER.setText("Withdraw " + noError);
                case "createCustomer" -> INFO_HANDLER.setText("Customer created " + noError);
                case "changeCustomerName" -> INFO_HANDLER.setText("Customername changed " + noError);
                case "delete customer" -> INFO_HANDLER.setText("Customer deleted " + noError);
                case "delete account" -> INFO_HANDLER.setText("Account deleted " + noError);
                case "savedFile" -> INFO_HANDLER.setText("File saved " + noError);
                case "loadingFile" -> INFO_HANDLER.setText("File loaded " + noError);
            }

        } else {
            INFO_HANDLER.setBackground(Color.red);
            switch (type) {
                case "deposit" -> INFO_HANDLER.setText("Deposit denied");
                case "withdraw" -> INFO_HANDLER.setText("Withdraw denied");
                case "createCustomer" -> INFO_HANDLER.setText("Customer not created");
                case "customerPno" -> INFO_HANDLER.setText("No customer chosen");
                case "customerAccount" -> INFO_HANDLER.setText("No chosen account");
                case "changeCustomerName" -> INFO_HANDLER.setText("Customername not changed due an error");
                case "emptyField" -> INFO_HANDLER.setText("No field is changed");
                case "No transactions" -> INFO_HANDLER.setText("No transactions made to the account");
                case "loadingFile" -> INFO_HANDLER.setText("File corrupt, loading file canceled");
                case "savedFile" -> INFO_HANDLER.setText("Something went wrong with saving the file");
                case "savedFilesSER" -> INFO_HANDLER.setText("File is wrong type, should end with .SER");
                case "savedFilesTXT" -> INFO_HANDLER.setText("File is wrong type, should end with .TXT");
            }
        }
    }

    /**
     * Helper för att nollställa midCenterPanel
     */
    private void clearMidPanel() {
        btnPanel.setVisible(false);
        createNewCustomerPanel.setVisible(false);
        createNewAccount.setVisible(false);
        changeNamePanel.setVisible(false);
        confirmDeleteCustomerPanel.setVisible(false);
        confirmDeleteAccountPanel.setVisible(false);
    }

    /**
     * Helper för att nollställa allt
     */
    private void clearScrn() {
        CUSTOMER_INFO.setText("");
        ACCOUNT_INFO.setText("");
        customerAccountNumber = 0;
        customerPno = null;
        customerList.setListData(bankLogic.getAllCustomers().toArray());
        accountList.setListData(new Vector());
        informationLog.setListData(new Vector());
    }

    /**
     * Nollställer infohandlerns utfall i botten
     */
    private void clearBottomLabel() {
        INFO_HANDLER.setText("");
    }

    /**
     * Grundkonfig fil för Gridbaglayouten. För att få symmetriska fält och knappar
     *
     * @return GridbagConstraints konfig mall
     */
    private GridBagConstraints buttonsGridBagConfig() {
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(0, 0, 5, 0);
        gridBagConstraints.ipady = 20;
        gridBagConstraints.ipadx = 130;
        return gridBagConstraints;

    }

    /**
     * Alla lyssnare uppdelade för att hitta enkelt vilka de tillhör
     */
    public class StartPageListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonText = e.getActionCommand();
            if (buttonText.equals("Employee")) {
                startPagePanel.setVisible(false);
                employeePanel.setVisible(true);
            }
        }
    }

    public class EmployeeMainBarListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            clearBottomLabel();
            clearMidPanel();

            String buttonText = e.getActionCommand();
            switch (buttonText) {
                case "Handle customer in bankregister" -> {
                    lHPanelSub.setVisible(false);
                    lHPanelMain.setVisible(true);
                }
                case "Handle accounts and transactions" -> {
                    lHPanelMain.setVisible(false);
                    lHPanelSub.setVisible(true);
                }
                case "Clear customer" -> clearScrn();
                case "Add new customer" -> createNewCustomerPanel.setVisible(true);
                case "Delete customer" -> confirmDeleteCustomerPanel.setVisible(true);
                case "Change Customer name" -> {
                    name.setText("");
                    surName.setText("");
                    changeNamePanel.setVisible(isCustomerPnoValid());
                }
                case "Create new account" -> createNewAccount.setVisible(isCustomerPnoValid());
                case "Make a transaction" -> btnPanel.setVisible(isCustomerPnoValid() && isCustomerAccountValid());
                case "Delete account" -> confirmDeleteAccountPanel.setVisible(true);
                case "See transactionhistory" -> getTransactions(true);
            }
        }
    }

    public class EmployeeSubBarListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String buttonText = e.getActionCommand();

            switch (buttonText) {
                case "Create Savingsaccount" -> createNewSaveAccount();
                case "Create Creditaccount" -> createNewCredAccount();
                case "Deposit" -> deposit();
                case "Withdraw" -> withDraw();
                case "Change name" -> changeCustomerName();
                case "Create customer" -> createCustomer();
                case "Confirm delete account" -> deleteAccount();
                case "Confirm delete customer" -> deleteCustomer();
            }

        }
    }

    /**
     * Klass som har hand om menyn.
     * Här finns actionlisteners som tar han om Save and Load
     */
    public class MenuListener implements ActionListener {
        SaveAndLoad saveAndLoad = new SaveAndLoad(bankLogic);

        @Override
        public void actionPerformed(ActionEvent e) {
            String menuText = e.getActionCommand();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("./marisk1_files"));


            switch (menuText) {
                case "Load Bankregister" -> {
                    fileChooser.setDialogTitle("Select a customerfile");
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Customer files .ser", "ser");
                    //Filtrerar bort ändelserna som inte heter .ser
                    fileChooser.addChoosableFileFilter(filter);
                    int response = fileChooser.showOpenDialog(null);
                    //Om det misslyckas med hämtningen får man -1 i retur då kickar Felhanteringen in.
                    if (response == JFileChooser.APPROVE_OPTION) {
                        File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                        //Hämtar Banklogic och om den är null kopplas felhantering in
                        bankLogic = saveAndLoad.loadBankRegisterToFile(file);
                        if (bankLogic == null) {
                            setInfoHandler(false, "loadingFile");
                            return;
                        }
                        customerList.setListData(bankLogic.getAllCustomers().toArray());
                        setInfoHandler(true, "loadingFile");
                    } else {
                        setInfoHandler(false, "loadingFile");
                    }
                }
                case "Load Transactions" -> {
                    fileChooser.setDialogTitle("Select a transactionfile");
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    //Skapar filterfunktion att man endast kan öppna txt filer.
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Transaction files .txt", "txt");
                    fileChooser.addChoosableFileFilter(filter);
                    //Sparar den öppnade filens svar i nummer för är den -1 har öppningen gått fel
                    int response = fileChooser.showOpenDialog(null);
                    if (response == JFileChooser.APPROVE_OPTION) {
                        //Läser in datan i File variabel genom filvägen, sparar sedan ned datan i en arraylista där
                        // jag splittar varje rad.
                        File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                        ArrayList<String> loadedFile = new ArrayList<>(saveAndLoad.loadTransactions(file));
                        //Validerar att kontot tillhör personnr innan utrendering. Felhanterare finns med.
                        if (bankLogic.validateLoadedTransactionFile(loadedFile)) {
                            popUpTransactionLog.setVisible(true);
                            popUpLog.setListData(loadedFile.toArray());
                        } else {
                            setInfoHandler(false, "loadingFile");
                        }
                    } else {
                        setInfoHandler(false, "loadingFile");
                    }

                }
                case "Save Bankregister" -> {
                    fileChooser.setDialogTitle("Save bankregister");
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Bankregister .ser", "ser");
                    fileChooser.addChoosableFileFilter(filter);
                    //Öppnar upp sparningen
                    int response = fileChooser.showSaveDialog(null);
                    if (response == JFileChooser.APPROVE_OPTION) {
                        File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                        //Kontrollerar att man skrivit in rätt ändelse och sedan om där finns dubblett;
                        if (file.toString().toLowerCase().endsWith(".ser")) {
                            if (file.exists()) {
                                //Om där finns dubblett spars filen i en tempFil sedan öppnas modal upp som sedan
                                //sköter vidare sparning med hjälp av tempfilen
                                tempFileBankregister = file;
                                popUpSaveDialog.setVisible(true);
                            } else {
                                setInfoHandler(bankLogic.saveBankRegisterToFile(file), "savedFile");
                            }
                        } else {
                            setInfoHandler(false, "savedFilesSER");
                        }
                    } else {
                        setInfoHandler(false, "savedFilesSER");
                    }
                }
                case "Save Transactions" -> {
                    tempFileBankregister = null;
                    fileChooser.setDialogTitle("Save transaction");
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Bankregister .txt", "txt");
                    fileChooser.addChoosableFileFilter(filter);

                    int response = fileChooser.showSaveDialog(null);
                    if (response == JFileChooser.APPROVE_OPTION) {
                        File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                        if (file.toString().toLowerCase().endsWith(".txt")) {
                            if (file.exists()) {
                                tempFileTransactions = file;
                                popUpSaveDialog.setVisible(true);

                            } else {
                                setInfoHandler(bankLogic.saveTransactionsToFile(file, customerPno, customerAccountNumber), "savedFile");
                            }
                        } else {
                            setInfoHandler(false, "savedFilesTXT");
                        }
                    } else {
                        setInfoHandler(false, "savedFilesTXT");
                    }
                }
                case "Log out" -> {
                    employeePanel.setVisible(false);
                    startPagePanel.setVisible(true);
                }
                case "Exit application" -> System.exit(0);
            }

        }

    }
}

