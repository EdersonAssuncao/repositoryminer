package br.edu.ufba.softvis.visminer.analyzer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import br.edu.ufba.softvis.visminer.analyzer.local.IRepositorySystem;
import br.edu.ufba.softvis.visminer.model.bean.Commit;
import br.edu.ufba.softvis.visminer.model.bean.Tree;
import br.edu.ufba.softvis.visminer.model.database.CommitDB;
import br.edu.ufba.softvis.visminer.model.database.RepositoryDB;
import br.edu.ufba.softvis.visminer.model.database.TreeDB;
import br.edu.ufba.softvis.visminer.persistence.dao.TreeDAO;
import br.edu.ufba.softvis.visminer.persistence.impl.TreeDAOImpl;
/**
 * @author Felipe Gustavo de Souza Gomes (felipegustavo1000@gmail.com)
 * @version 0.9
 * @see CommitAnalyzer
 * @see CommitterAnalyzer
 * @see FileAnalyzer
 * @see IssueAnalyzer
 * @see MilestoneAnalyzer
 * @see RepositoryAnalyzer
 * @see IAnalyzer
 * 
 * Defines how to save or to increment informations about trees in repository.
 */
public class TreeAnalyzer implements IAnalyzer<Void>{

	@SuppressWarnings("unchecked")
	@Override
	public Void persist(Object... objects) {

		List<CommitDB> commitsDb = (List<CommitDB>) objects[0];
		RepositoryDB repositoryDb = (RepositoryDB) objects[1];
		IRepositorySystem repoSys = (IRepositorySystem) objects[2];
		EntityManager entityManager = (EntityManager) objects[3];
		
		TreeDAO treeDao = new TreeDAOImpl();
		treeDao.setEntityManager(entityManager);
		
		List<TreeDB> treesDb = new ArrayList<TreeDB>();
		
		CommitDB commitDb = new CommitDB();
		
		for(Tree tree : repoSys.getTrees()){
			
			TreeDB treeDb = new TreeDB(0, tree.getFullName(), tree.getLastUpdate(),
					tree.getName(), tree.getType());
			treeDb.setRepository(repositoryDb);
			treeDb.setCommits(new ArrayList<CommitDB>());
			
			for(Commit commit : repoSys.getCommitsByTree(tree.getFullName())){
				commitDb.setName(commit.getName());
				int index = commitsDb.indexOf(commitDb);
				treeDb.getCommits().add(commitsDb.get(index));
			}
			
			treesDb.add(treeDb);
		}
		
		treeDao.batchSave(treesDb);
		return null;
		
	}

	@Override
	public Void increment(Object... objects) {
		// TODO Auto-generated method stub
		return null;
	}

}