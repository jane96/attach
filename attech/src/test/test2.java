import com.sccl.attech.modules.sys.dao.MenuDao;
import com.sccl.attech.modules.sys.entity.Menu;

public class test2 {
	
	public static void main(String[] args) {
		//Menu c1 = new Menu(parentIds, name, href, target, icon, sort, isShow, isActiviti, isMobile, permission, plugins, parentId)
		Menu entity = new Menu( "0,1", "投票管理", "", "", "", 3, "1", "0","0", "", "", "1");
		MenuDao menuDao = new MenuDao();
		try{
		menuDao.save(entity);
		System.out.println("插入成功");
		}catch(Exception e){
			System.out.println("出错了");
		}
	}
}
