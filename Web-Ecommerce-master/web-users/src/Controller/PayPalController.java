package Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import BusinessLogics.gioHangBL;
import BusinessLogics.hoaDonBL;
import Encrypt.encrypt;
import JavaBeans.hoaDon;
import JavaBeans.sanPhamMua;
import JavaBeans.user;


@WebServlet("/PayPalController")
public class PayPalController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public PayPalController() {
        super();
    }
    
    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
    
    public static class DottedCell implements PdfPCellEvent {
        public void cellLayout(PdfPCell cell, Rectangle position,
                               PdfContentByte[] canvases) {
            PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
            canvas.setLineDash(3f, 3f);
            canvas.moveTo(position.getLeft(), position.getTop());
            canvas.lineTo(position.getRight(), position.getTop());
            canvas.moveTo(position.getLeft(), position.getBottom());
            canvas.lineTo(position.getRight(), position.getBottom());
            canvas.stroke();
        }
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		hoaDon hd = (hoaDon) session.getAttribute("hoaDon");
		gioHangBL gioHang = (gioHangBL) session.getAttribute("gioHang");
		DecimalFormat df = new DecimalFormat("###,###.##");
		PrintWriter out = response.getWriter();
        try {
        	// send email
    		final String username = "chamsockhachhangdtonline@gmail.com";
            final String password = "Tuminhhau";

            // Create PDF     
            Document document = new Document();

            final String path = getServletContext().getRealPath(".");
            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            String title = removeAccent(hd.getTen_user())+".pdf";

            final File file = new File(dir, title);
            FileOutputStream fOut = new FileOutputStream(file);

			PdfWriter.getInstance(document, fOut);
			document.open();

	        DottedLineSeparator separatorTop = new DottedLineSeparator();
	        separatorTop.setPercentage(59500f / 523f);
	        Chunk linebreakTop = new Chunk(separatorTop);
	        document.add(linebreakTop);

	        Paragraph p1 = new Paragraph("Cua hang dien thoai online");
	        p1.setAlignment(Element.ALIGN_CENTER);
	        document.add(p1);

	        Paragraph p2 = new Paragraph("Phone: 9943123999");
	        p2.setAlignment(Element.ALIGN_CENTER);
	        document.add(p2);

	        Paragraph p3 = new Paragraph("Email : chamsockhachhangdtonline@gmail.com");
	        p3.setAlignment(Element.ALIGN_CENTER);
	        document.add(p3);


	        DottedLineSeparator separator = new DottedLineSeparator();
	        separator.setPercentage(59500f / 523f);
	        Chunk linebreak = new Chunk(separator);
	        document.add(linebreak);

	        Paragraph paragraph = new Paragraph("Ma hoa don: " + 1);
	        paragraph.setAlignment(Element.ALIGN_LEFT);
	        document.add(paragraph);

	        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	        String billCreated = sdf.format(new Date());

	        paragraph = new Paragraph("Ngay tao hoa don: " + billCreated );
	        paragraph.setAlignment(Element.ALIGN_LEFT);
	        document.add(paragraph);

	        paragraph = new Paragraph("Nguoi nhan: " + removeAccent(hd.getTen_user()));
	        paragraph.setAlignment(Element.ALIGN_LEFT);
	        document.add(paragraph);

	        paragraph = new Paragraph("SDT: " + hd.getSdt());
	        paragraph.setAlignment(Element.ALIGN_LEFT);
	        document.add(paragraph);

	        paragraph = new Paragraph("Dia chi giao hang: " + hd.getDiaChi());
	        paragraph.setAlignment(Element.ALIGN_LEFT);
	        document.add(paragraph);

	        paragraph = new Paragraph("Hinh thuc thanh toan: " + removeAccent(hd.getHinhThucThanhToan()));
	        paragraph.setAlignment(Element.ALIGN_LEFT);
	        document.add(paragraph);

	        DottedLineSeparator separator2 = new DottedLineSeparator();
	        separator2.setPercentage(59500f / 523f);
	        Chunk linebreak2 = new Chunk(separator2);
	        document.add(linebreak2);

	        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);


	        PdfPTable table = new PdfPTable(5);
	        table.setTotalWidth(300f);
	        table.setHorizontalAlignment(Element.ALIGN_CENTER);

	        PdfPCell cell1 = new PdfPCell(new Phrase("STT", boldFont));
	        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
	        cell1.setCellEvent(new DottedCell());
	        cell1.setBorder(Rectangle.NO_BORDER);
	        table.addCell(cell1);

	        PdfPCell cell2 = new PdfPCell(new Phrase("Ten san pham", boldFont));
	        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	        cell2.setCellEvent(new DottedCell());
	        cell2.setBorder(Rectangle.NO_BORDER);
	        table.addCell(cell2);

	        PdfPCell cell3 = new PdfPCell(new Phrase("Gia 1 cai", boldFont));
	        cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        cell3.setCellEvent(new DottedCell());
	        cell3.setBorder(Rectangle.NO_BORDER);
	        table.addCell(cell3);

	        PdfPCell cell4 = new PdfPCell(new Phrase("So luong", boldFont));
	        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell4.setCellEvent(new DottedCell());
	        cell4.setBorder(Rectangle.NO_BORDER);
	        table.addCell(cell4);

	        PdfPCell cell5 = new PdfPCell(new Phrase("Tong", boldFont));
	        cell5.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        cell5.setCellEvent(new DottedCell());
	        cell5.setBorder(Rectangle.NO_BORDER);
	        table.addCell(cell5);
	        //table.setHeaderRows(1);

	        
	        List<sanPhamMua> dsSanPhamMua = gioHang.danhSachSanPhamMua();

	        int i = 0;
	        for(sanPhamMua sanPhamMua : dsSanPhamMua){

	            cell1 = new PdfPCell(new Phrase(String.valueOf(i + 1)));
	            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
	            cell1.setBorder(Rectangle.NO_BORDER);
	            table.addCell(cell1);

	            cell2 = new PdfPCell(new Phrase(sanPhamMua.getTenSanPham()));
	            cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
	            cell2.setBorder(Rectangle.NO_BORDER);
	            table.addCell(cell2);

	            cell3 = new PdfPCell(new Phrase(df.format(sanPhamMua.getGiaSanPham())+" ??"));
	            cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
	            cell3.setBorder(Rectangle.NO_BORDER);
	            table.addCell(cell3);

	            cell4 = new PdfPCell(new Phrase(String.valueOf(sanPhamMua.getSoLuongMua())));
	            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell4.setBorder(Rectangle.NO_BORDER);
	            table.addCell(cell4);

	            cell5 = new PdfPCell(new Phrase(df.format(sanPhamMua.getThanhTien())+" ??"));
	            cell5.setHorizontalAlignment(Element.ALIGN_RIGHT);
	            cell5.setBorder(Rectangle.NO_BORDER);
	            table.addCell(cell5);
	            //c.moveToNext();
	            i++;
	        }

	        cell1 = new PdfPCell(new Phrase(""));
	        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell1.setCellEvent(new DottedCell());
	        cell1.setBorder(Rectangle.NO_BORDER);
	        table.addCell(cell1);

	        cell2 = new PdfPCell(new Phrase(""));
	        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell2.setCellEvent(new DottedCell());
	        cell2.setBorder(Rectangle.NO_BORDER);
	        table.addCell(cell2);

	        cell3 = new PdfPCell(new Phrase(""));
	        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell3.setCellEvent(new DottedCell());
	        cell3.setBorder(Rectangle.NO_BORDER);
	        table.addCell(cell3);

	        cell4 = new PdfPCell(new Phrase("Tong cong: ", boldFont));
	        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell4.setCellEvent(new DottedCell());
	        cell4.setBorder(Rectangle.NO_BORDER);
	        table.addCell(cell4);

	        cell5 = new PdfPCell(new Phrase(df.format(gioHang.tongTien())+" ??", boldFont));
	        cell5.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        cell5.setCellEvent(new DottedCell());
	        cell5.setBorder(Rectangle.NO_BORDER);
	        table.addCell(cell5);

	        document.add(table);

	        
	        document.close();

	        
	        Properties props = new Properties();
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.smtp.port", "587");

	        Session sessions = Session.getInstance(props,
	                new javax.mail.Authenticator() {
	                    protected PasswordAuthentication getPasswordAuthentication() {
	                        return new PasswordAuthentication(username, password);
	                    }
	                });

	        try {
	            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
	            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
	            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
	            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
	            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
	            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
	            CommandMap.setDefaultCommandMap(mc);

	            MimeMessage message = new MimeMessage(sessions);


	            message.setFrom(new InternetAddress("chamsockhachhangdtonline@gmail.com"));
	            message.setRecipients(Message.RecipientType.TO,
	                    InternetAddress.parse(hd.getEmail()));
	            message.setHeader("Content-Type", "text/plain; charset=UTF-8");
	            message.setSubject("[MobileShop] Th??ng tin ????n h??ng");
	            //String path = Environment.getExternalStorageDirectory().toString() + "/Image-" + s + ".jpg";

	            Multipart multipart = new MimeMultipart();
	            MimeBodyPart attachementPart = new MimeBodyPart();
	            attachementPart.attachFile(new File(String.valueOf(file)));
	            multipart.addBodyPart(attachementPart);
	            String details = "", sp_mua = "";
	            for (sanPhamMua spm : gioHang.danhSachSanPhamMua()) {
	                details += "T??n s???n ph???m: " + spm.getTenSanPham() + "- S??? l?????ng : " + spm.getSoLuongMua() + "\n" + "Gi?? 1 c??i: " + df.format(spm.getGiaSanPham());
	                sp_mua += "T??n s???n ph???m: " + spm.getTenSanPham() + " - S??? l?????ng : " + spm.getSoLuongMua() + " - " + "Gi?? 1 c??i: " + df.format(spm.getGiaSanPham()) + "??<br/>";
	            }

	            MimeBodyPart textPart = new MimeBodyPart();
	            String noiDung = "Ch??o b???n, " + hd.getTen_user()
	                    + "<br/> C???m ??n b???n ???? mua h??ng c???a ch??ng t??i"
	                    + "<br/> Sau ????y l?? chi ti???t ????n h??ng b???n ???? mua: "
	                    + "<br/> <br/>" + " <strong> " + sp_mua + "</strong>"
	                    + "<br/> <br/>" + " <strong> T???ng ti???n: " + df.format(gioHang.tongTien()) + " VN??</strong>"
	                    + "<br/> File PDF vui l??ng tham kh???o t???p tin ????nh k??m ph??a d?????i."
	                    + "<br/> <br/> Ban Qu???n L??, \nVHN!";
	            textPart.setText(noiDung);
	            textPart.setContent(noiDung, "text/html; charset=utf-8");
	            multipart.addBodyPart(textPart);

	            //message.setText(noiDung);
	            message.setContent(multipart, "multipart/*; charset=utf-8");
	            Transport.send(message);
	            
	            // Insert to DB
	            user u = (user) session.getAttribute("member");
	            int idUser = u.getIduser();
	            String key = "Bar12345Bar12345"; // 128 bit key
                String initVector = "RandomInitVector"; // 16 bytes IV
	            hoaDon hdInsert = new hoaDon();
	            hdInsert.setTen_user(hd.getTen_user());
	            hdInsert.setHo_user(hd.getHo_user());
	            hdInsert.setEmail(hd.getEmail());
	            hdInsert.setSdt(hd.getSdt());
	            hdInsert.setId_user(idUser);
	            hdInsert.setDiaChi(encrypt.encryptAES(key, initVector, hd.getDiaChi()));
	            hdInsert.setQuan(encrypt.encryptAES(key, initVector, hd.getQuan()));
	            hdInsert.setPhuong(encrypt.encryptAES(key, initVector, hd.getPhuong()));
	            hdInsert.setChiTiet(encrypt.encryptAES(key, initVector, details));
	            hdInsert.setHinhThucThanhToan(hd.getHinhThucThanhToan());
	            int insert = hoaDonBL.themHoaDon(hdInsert);
	            
	            response.sendRedirect("trang-chu");

	        } catch (MessagingException e) {

	            throw new RuntimeException(e);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
