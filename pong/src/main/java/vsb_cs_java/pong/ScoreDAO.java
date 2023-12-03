package vsb_cs_java.pong;

import static java.util.stream.Collectors.toSet;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class ScoreDAO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String SQL_CREATE_H2 = "CREATE TABLE IF NOT EXISTS score ("
			+ "id INT AUTO_INCREMENT PRIMARY KEY, "
			+ "p1_name VARCHAR(255),"
			+ "p1_score INT NOT NULL,"
			+ "p2_name VARCHAR(255),"
			+ "p2_score INT NOT NULL"
			+ ")";
	
	public ScoreDAO() {
		try (Connection conn = getConnection()) {
			createTable(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Score> load() {
		List<Score> result = new LinkedList<>();
		try(Connection connection = getConnection(); 
			Statement stmt = connection.createStatement()) {
			
			try(ResultSet rs = stmt.executeQuery("SELECT id, p1_name, p1_score, p2_name, p2_score FROM score")) {
				while (rs.next()) { 
					result.add(new Score(rs.getInt(1), rs.getString(2),  rs.getInt(3), rs.getString(4),  rs.getInt(5)));
				}
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void saveScore(Score score) {
		
		try(Connection conn = getConnection()){ 
					
			try(PreparedStatement pstms = conn.prepareStatement("INSERT INTO score (p1_name, p1_score, p2_name, p2_score) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS))
			{	
				if (score.getId() == null) {
					pstms.setString(1, score.getPl1Name());
					pstms.setInt(2, score.getPl1Score());
					pstms.setString(3, score.getPl2Name());
					pstms.setInt(4, score.getPl2Score());
					pstms.execute();
					ResultSet rs =  pstms.getGeneratedKeys();
					if (rs.next()) {
						score.setId(rs.getInt(1));
					}
				}
			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void save(List<Score> scores) {
		
		Set<Integer> m = scores.stream()
				   .filter(s -> s.getId() != null)
				   .map(Score::getId)
				   .collect(toSet());
		
		Set<Integer> d = load().stream().map(Score::getId).collect(Collectors.toSet());
		d.removeAll(m);
		Set<Integer> idsOnlyInDB = d;
		delete(idsOnlyInDB);
		List<Score> scoreSaved = new LinkedList<>();
		
		try(Connection conn = getConnection();
					PreparedStatement pstms = conn.prepareStatement("INSERT INTO score (p1_name, p1_score, p2_name, p2_score) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)){
				for (Score score: scores) {
					if (score.getId() == null) {
						pstms.setString(1, score.getPl1Name());
						pstms.setInt(2, score.getPl1Score());
						pstms.setString(3, score.getPl2Name());
						pstms.setInt(4, score.getPl2Score());
						pstms.addBatch();
						scoreSaved.add(score);
					}
				}
				if (!scoreSaved.isEmpty()) {
					pstms.executeBatch();
					ResultSet rs =  pstms.getGeneratedKeys();
					Iterator<Score> iter = scoreSaved.iterator();
					while (rs.next()) {
						Score score = iter.next();
						score.setId(rs.getInt(1));
					}
				}
				return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void delete(Set<Integer> idsForDelete) {
			try(Connection conn = getConnection();
					PreparedStatement pstms = conn.prepareStatement("DELETE FROM score WHERE id = ?")){
				for (Integer id: idsForDelete) {
					pstms.setInt(1, id);
					pstms.addBatch();
				}
				pstms.executeBatch();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private void createTable(Connection conn) {
		try(Statement stmt = conn.createStatement()) {
			stmt.execute(SQL_CREATE_H2);
		} catch (SQLException e) {
			if (e.getSQLState().equals("X0Y32")) {
				//ignore because table already exists
				return;
			}
			e.printStackTrace();
		}
		
	}

	private Connection getConnection() {
		try {
			return DriverManager.getConnection("jdbc:h2:./scoreDBh2;AUTO_SERVER=TRUE", "sa","");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}